package gropius.sync.github

import com.apollographql.apollo3.ApolloClient
import gropius.model.architecture.IMS
import gropius.model.issue.Issue
import gropius.model.issue.Label
import gropius.model.issue.timeline.*
import gropius.model.user.GropiusUser
import gropius.model.user.IMSUser
import gropius.model.user.User
import gropius.repository.issue.IssueRepository
import gropius.repository.user.IMSUserRepository
import gropius.sync.github.generated.*
import gropius.sync.github.generated.MutateAddLabelMutation.Data.AddLabelsToLabelable.Labelable.Companion.asIssue
import gropius.sync.github.generated.MutateCreateLabelMutation.Data.CreateLabel.Label.Companion.labelData
import gropius.sync.github.generated.MutateRemoveLabelMutation.Data.RemoveLabelsFromLabelable.Labelable.Companion.asIssue
import gropius.sync.github.model.IssueInfo
import gropius.sync.github.repository.IssueInfoRepository
import gropius.sync.github.repository.LabelInfoRepository
import gropius.sync.github.repository.TimelineEventInfoRepository
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.reactor.awaitSingle
import org.neo4j.cypherdsl.core.Cypher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Stateless component for the outgoing part of the sync
 * @param issueInfoRepository Reference for the spring instance of IssueInfoRepository
 * @param timelineEventInfoRepository Reference for the spring instance of TimelineEventInfoRepository
 * @param helper Reference for the spring instance of JsonHelper
 * @param tokenManager Reference for the spring instance of TokenManager
 * @param issueRepository Reference for the spring instance of IssueRepository
 * @param imsUserRepository Reference for the spring instance of IMSUserRepository
 * @param incoming Reference for the spring instance of Incoming
 */
@Component
class Outgoing(
    private val issueInfoRepository: IssueInfoRepository,
    private val timelineEventInfoRepository: TimelineEventInfoRepository,
    private val helper: JsonHelper,
    private val tokenManager: TokenManager,
    private val issueRepository: IssueRepository,
    private val imsUserRepository: IMSUserRepository,
    private val incoming: Incoming,
    private val nodeSourcerer: NodeSourcerer,
    private val labelInfoRepository: LabelInfoRepository
) {
    /**
     * Logger used to print notifications
     */
    private val logger = LoggerFactory.getLogger(Outgoing::class.java)

    /**
     * Find the last consecutive list of blocks of the same close/reopen type
     * @param relevantTimeline List of timeline items filtered for issue and sorted by date
     * @return Consecutive same type timeline items
     */
    private fun findFinalTypeBlock(relevantTimeline: List<TimelineItem>): List<TimelineItem> {
        val lastItem = relevantTimeline.last()
        val finalItems = mutableListOf<TimelineItem>()
        for (item in relevantTimeline.reversed()) {
            if (item::class != lastItem::class) {
                break
            }
            finalItems += item
        }
        return finalItems
    }

    /**
     * Find token for an IMSUser or an IMSUser on the same IMS with same username
     * @param user user to search token for
     * @param imsProjectConfig active config
     * @return string if token found, null otherwise
     */
    private suspend fun extractIMSUserToken(imsProjectConfig: IMSProjectConfig, user: IMSUser): String? {
        if (user.ims().value == imsProjectConfig.imsConfig.ims) {
            val token = tokenManager.getGithubUserToken(user)
            if (token != null) {
                return token
            }
            if (user.username?.isEmpty() != false) {
                return null
            }
            val node = Cypher.node(IMSUser::class.simpleName).named("iMSUser")
            val imsNode = Cypher.node(IMS::class.simpleName)
                .withProperties(mapOf("id" to Cypher.anonParameter(imsProjectConfig.imsConfig.ims.rawId!!)))
            val imsCondition = node.relationshipFrom(imsNode, IMS.USER).asCondition()
            val usernameCondition = node.property("username").eq(Cypher.anonParameter(user.username))
            for (imsUser in imsUserRepository.findAll(imsCondition.and(usernameCondition)).collectList()
                .awaitSingle()) {
                val imsUserToken = tokenManager.getGithubUserToken(imsUser)
                if (imsUserToken != null) {
                    return imsUserToken
                }
            }
        }
        return null
    }

    /**
     * Find token for a User or any similar enough user
     * @param user user to search token for
     * @param imsProjectConfig active config
     * @return string if token found, null otherwise
     */
    private suspend fun extractUserToken(imsProjectConfig: IMSProjectConfig, user: User): String? {
        var activeGropiusUser: GropiusUser
        if (user is IMSUser) {
            val token = extractIMSUserToken(imsProjectConfig, user)
            if (token != null) {
                return token
            }
            val gropiusUser = user.gropiusUser().value
            if (gropiusUser == null) {
                return null
            } else {
                activeGropiusUser = gropiusUser
            }
        } else if (user is GropiusUser) {
            activeGropiusUser = user
        }
        //TODO("Search gropius user")
        return null
    }

    /**
     * Create client to mutate on GitHub working as one of the users or the fallback
     * @param imsProjectConfig active config
     * @param userList List of users which have provided changes
     * @return the client initialized with a token for writing
     */
    private suspend fun createClient(imsProjectConfig: IMSProjectConfig, userList: Iterable<User>): ApolloClient {
        var token: String? = null
        for (user in userList) {
            token = extractUserToken(imsProjectConfig, user)
        }
        if (token == null) {
            token = tokenManager.getTokenForIMSUser(imsProjectConfig.imsConfig, null)
        }
        return ApolloClient.Builder().serverUrl(imsProjectConfig.imsConfig.graphQLUrl.toString())
            .addHttpHeader("Authorization", "bearer $token")
            .addHttpHeader("Accept", "application/json, application/vnd.github.bane-preview+json").build()
    }

    /**
     * Mutate an ReopenIssue upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param userList users that have contributed to the event
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun githubReopenIssue(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        logger.info("Scheduling reopening ${issueInfo.neo4jId}")
        return listOf {
            val client = createClient(imsProjectConfig, userList)
            val response = client.mutation(MutateReopenIssueMutation(issueInfo.githubId)).execute()
            val item = response.data?.closeIssue?.issue?.timelineItems?.nodes?.lastOrNull()
            if (item != null) {
                incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
            }
        }
    }

    /**
     * Mutate an CloseIssue upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param userList users that have contributed to the event
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun githubCloseIssue(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        logger.info("Scheduling closing ${issueInfo.neo4jId}")
        return listOf {
            val client = createClient(imsProjectConfig, userList)
            val response = client.mutation(MutateCloseIssueMutation(issueInfo.githubId)).execute()
            val item = response.data?.closeIssue?.issue?.timelineItems?.nodes?.lastOrNull()
            if (item != null) {
                incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
            }
        }
    }

    /**
     * Mutate an AddedLabelEvent upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param userList users that have contributed to the event
     * @param label the label that has been added
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun githubAddLabel(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, label: Label, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        logger.info("Scheduling adding ${label.name} (${label.rawId}) to ${issueInfo.neo4jId}")
        val labelId = labelInfoRepository.findByNeo4jId(label.rawId!!)
        if (labelId != null) {
            return if (labelId.url == imsProjectConfig.url) {
                listOf {
                    val client = createClient(imsProjectConfig, userList)
                    val response =
                        client.mutation(MutateAddLabelMutation(issueInfo.githubId, labelId.githubId)).execute()
                    val item =
                        response.data?.addLabelsToLabelable?.labelable?.asIssue()?.timelineItems?.nodes?.lastOrNull()
                    if (item != null) {
                        incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
                    }
                }
            } else {
                emptyList()
            }
        } else {
            return listOf {
                val client = createClient(imsProjectConfig, userList)
                val createLabelResponse = client.mutation(
                    MutateCreateLabelMutation(
                        issueInfo.githubId, label.name, label.description, label.color
                    )
                ).execute()
                val newLabel = createLabelResponse?.data?.createLabel?.label?.labelData()
                if (newLabel != null) {
                    nodeSourcerer.ensureLabel(imsProjectConfig, newLabel)
                    val response = client.mutation(MutateAddLabelMutation(issueInfo.githubId, newLabel.id)).execute()
                    val item =
                        response.data?.addLabelsToLabelable?.labelable?.asIssue()?.timelineItems?.nodes?.lastOrNull()
                    if (item != null) {
                        incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
                    }
                }
            }
        }
    }

    /**
     * Mutate an RemovedLabelEvent upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param userList users that have contributed to the event
     * @param label the label that has been removed
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun githubRemoveLabel(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, label: Label, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        logger.info("Scheduling removing ${label.name} (${label.rawId}) from ${issueInfo.neo4jId}")
        return listOf {
            val labelId = labelInfoRepository.findByNeo4jId(label.rawId!!)
            if (labelId != null) {
                val client = createClient(imsProjectConfig, userList)
                val response =
                    client.mutation(MutateRemoveLabelMutation(issueInfo.githubId, labelId.githubId)).execute()
                val item =
                    response.data?.removeLabelsFromLabelable?.labelable?.asIssue()?.timelineItems?.nodes?.lastOrNull()
                if (item != null) {
                    incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
                }
            }
        }
    }

    /**
     * Mutate the open/close state of an issue upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param timeline TimelineItems for this issue
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun pushReopenClose(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, timeline: List<TimelineItem>
    ): List<suspend () -> Unit> {
        val relevantTimeline = timeline.filter { (it is ReopenedEvent) || (it is ClosedEvent) }
        if (relevantTimeline.isEmpty()) {
            return listOf()
        }
        val finalBlock = findFinalTypeBlock(relevantTimeline)
        for (item in finalBlock) {
            if (timelineEventInfoRepository.findByNeo4jId(item.rawId!!) != null) {
                return listOf()
            }
        }
        val collectedMutations = mutableListOf<suspend () -> Unit>()
        if (shouldSyncType<ReopenedEvent, ClosedEvent>(
                finalBlock, relevantTimeline, true
            )
        ) {
            collectedMutations += githubReopenIssue(imsProjectConfig,
                issueInfo,
                finalBlock.map { it.lastModifiedBy().value })
        }
        if (shouldSyncType<ClosedEvent, ReopenedEvent>(
                finalBlock, relevantTimeline, false
            )
        ) {
            collectedMutations += githubCloseIssue(
                imsProjectConfig,
                issueInfo,
                finalBlock.map { it.lastModifiedBy().value })
        }
        return collectedMutations
    }

    /**
     * Check if TimelineItem should be synced or ignored
     * @param AddingItem Item type with the same semantic as the item to add
     * @param RemovingItem Item type invalidating the AddingItem
     * @param finalBlock the last block of similar items that should be checked for syncing
     * @param relevantTimeline Sorted part of the timeline containing only TimelineItems interacting with finalBlock
     * @param restoresDefaultState if the timeline item converges the state of the issue towards the state of an empty issue
     * @return true if and only if there are unsynced changes that should be synced to GitHub
     */
    private suspend inline fun <reified AddingItem : TimelineItem, reified RemovingItem : TimelineItem> shouldSyncType(
        finalBlock: List<TimelineItem>, relevantTimeline: List<TimelineItem>, restoresDefaultState: Boolean
    ): Boolean {
        if (finalBlock.last() is AddingItem) {
            val lastNegativeEvent = relevantTimeline.filterIsInstance<AddingItem>()
                .lastOrNull { timelineEventInfoRepository.findByNeo4jId(it.rawId!!)?.githubId != null }
            if (lastNegativeEvent == null) {
                return !restoresDefaultState
            } else {
                if (relevantTimeline.filterIsInstance<RemovingItem>()
                        .filter { it.lastModifiedAt > lastNegativeEvent.lastModifiedAt }
                        .firstOrNull { timelineEventInfoRepository.findByNeo4jId(it.rawId!!)?.githubId != null } == null
                ) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Mutate the labels of an issue upto GitHub
     * @param imsProjectConfig active config
     * @param issueInfo info of the issue containing the timeline item
     * @param timeline TimelineItems for this issue
     * @return List of functions that contain the actual mutation executors
     */
    private suspend fun pushLabels(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, timeline: List<TimelineItem>
    ): List<suspend () -> Unit> {
        val groups = timeline.filter { (it is AddedLabelEvent) || (it is RemovedLabelEvent) }.groupBy {
            when (it) {
                is AddedLabelEvent -> {
                    it.addedLabel().value
                }

                is RemovedLabelEvent -> {
                    it.removedLabel().value
                }

                else -> throw IllegalStateException()
            }
        }
        val collectedMutations = mutableListOf<suspend () -> Unit>()
        for ((label, relevantTimeline) in groups) {
            val finalBlock = findFinalTypeBlock(relevantTimeline)
            for (item in finalBlock) {
                if (timelineEventInfoRepository.findByNeo4jId(item.rawId!!) != null) {
                    return listOf()
                }
            }
            if (shouldSyncType<AddedLabelEvent, RemovedLabelEvent>(
                    finalBlock, relevantTimeline, false
                )
            ) {
                collectedMutations += githubAddLabel(
                    imsProjectConfig,
                    issueInfo,
                    label,
                    finalBlock.map { it.lastModifiedBy().value })
            }
            if (shouldSyncType<RemovedLabelEvent, AddedLabelEvent>(
                    finalBlock, relevantTimeline, true
                )
            ) {
                collectedMutations += githubRemoveLabel(imsProjectConfig,
                    issueInfo,
                    label,
                    finalBlock.map { it.lastModifiedBy().value })
            }
        }
        return collectedMutations
    }

    /**
     * Check a modified issue for mutateable changes
     * @param issue Issue to check
     * @param imsProjectConfig active config
     * @param issueInfo IssueInfo containing the IssueInfo used for writing updates back to mongo
     */
    suspend fun issueModified(
        imsProjectConfig: IMSProjectConfig, issue: Issue, issueInfo: IssueInfo
    ): List<suspend () -> Unit> {
        val collectedMutations = mutableListOf<suspend () -> Unit>()
        val timeline = issue.timelineItems().toList().sortedBy { it.lastModifiedAt }
        collectedMutations += pushReopenClose(imsProjectConfig, issueInfo, timeline)
        collectedMutations += pushLabels(imsProjectConfig, issueInfo, timeline)
        return collectedMutations
    }

    /**
     * Sync issues of one IMSProject
     * @param imsProjectConfig the config of the IMSProject
     */
    suspend fun syncIssues(imsProjectConfig: IMSProjectConfig) {
        val collectedMutations = mutableListOf<suspend () -> Unit>()
        for (issueInfo in issueInfoRepository.findByUrl(imsProjectConfig.url).toSet()) {
            val issue = issueRepository.findById(issueInfo.neo4jId).awaitSingle()
            if ((issueInfo.lastOutgoingSync == null) || (issue.lastUpdatedAt != issueInfo.lastOutgoingSync)) {
                collectedMutations += issueModified(imsProjectConfig, issue, issueInfo)
                issueInfo.lastOutgoingSync = issue.lastUpdatedAt
                issueInfoRepository.save(issueInfo).awaitSingle()
            }
        }
        if (collectedMutations.size > 100) {//TODO: Config
            throw SyncNotificator.NotificatedError("SYNC_GITHUB_TOO_MANY_MUTATIONS")
        }
        logger.info("Pushing ${collectedMutations.size} mutations")
        for (mutation in collectedMutations) {
            mutation()
        }
    }
}