package gropius.sync.github

import com.apollographql.apollo3.ApolloClient
import gropius.model.architecture.IMS
import gropius.model.issue.Issue
import gropius.model.issue.timeline.ClosedEvent
import gropius.model.issue.timeline.ReopenedEvent
import gropius.model.issue.timeline.TimelineItem
import gropius.model.user.GropiusUser
import gropius.model.user.IMSUser
import gropius.model.user.User
import gropius.repository.issue.IssueRepository
import gropius.repository.issue.timeline.TimelineItemRepository
import gropius.repository.user.IMSUserRepository
import gropius.sync.IssueCleaner
import gropius.sync.github.generated.MutateCloseIssueMutation
import gropius.sync.github.generated.MutateReopenIssueMutation
import gropius.sync.github.model.IssueInfo
import gropius.sync.github.repository.*
import io.github.graphglue.definition.NodeDefinitionCollection
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.reactor.awaitSingle
import org.neo4j.cypherdsl.core.Cypher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Stateless component for the incoming part of the sync
 * @param helper Reference for the spring instance of JsonHelper
 * @param imsConfigManager Reference for the spring instance of IMSConfigManager
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 * @param syncNotificator Reference for the spring instance of SyncNotificator
 * @param tokenManager Reference for the spring instance of TokenManager
 * @param repositoryInfoRepository Reference for the spring instance of RepositoryInfoRepository
 * @param issueInfoRepository Reference for the spring instance of IssueInfoRepository
 * @param userInfoRepository Reference for the spring instance of UserInfoRepository
 * @param labelInfoRepository Reference for the spring instance of LabelInfoRepository
 * @param mongoOperations Reference for the spring instance of ReactiveMongoOperations
 * @param timelineEventInfoRepository Reference for the spring instance of TimelineEventInfoRepository
 * @param issueCleaner Reference for the spring instance of IssueCleaner
 * @param nodeSourcerer Reference for the spring instance of NodeSourcerer
 * @param timelineItemHandler Reference for the spring instance of TimelineItemHandler
 */
@Component
class Outgoing(
    private val repositoryInfoRepository: RepositoryInfoRepository,
    private val issueInfoRepository: IssueInfoRepository,
    private val userInfoRepository: UserInfoRepository,
    private val labelInfoRepository: LabelInfoRepository,
    private val mongoOperations: ReactiveMongoOperations,
    private val timelineEventInfoRepository: TimelineEventInfoRepository,
    private val issueCleaner: IssueCleaner,
    private val nodeSourcerer: NodeSourcerer,
    private val timelineItemHandler: TimelineItemHandler,
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
    private val helper: JsonHelper,
    private val imsConfigManager: IMSConfigManager,
    private val syncNotificator: SyncNotificator,
    private val tokenManager: TokenManager,
    private val issueRepository: IssueRepository,
    private val timelineItemRepository: TimelineItemRepository,
    private val imsUserRepository: IMSUserRepository,
    private val nodeDefinitionCollection: NodeDefinitionCollection,
    private val incoming: Incoming
) {
    /**
     * Logger used to print notifications
     */
    private val logger = LoggerFactory.getLogger(Outgoing::class.java)

    private fun findFinalTypeBlock(relevantTimeline: List<TimelineItem>): List<TimelineItem> {
        val lastItem = relevantTimeline.last()
        val finalItems = mutableListOf<TimelineItem>()
        for (item in relevantTimeline.reversed()) {
            if ((item is ReopenedEvent) != (lastItem is ReopenedEvent)) break;
            finalItems += item
        }
        return finalItems
    }

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
                val token = tokenManager.getGithubUserToken(imsUser)
                if (token != null) {
                    return token
                }
            }
        }
        return null
    }

    private suspend fun extractUser(imsProjectConfig: IMSProjectConfig, user: User): String? {
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

    private suspend fun createClient(imsProjectConfig: IMSProjectConfig, userList: Iterable<User>): ApolloClient {
        var token: String? = null
        for (user in userList) {
            token = extractUser(imsProjectConfig, user)
        }
        if (token == null) {
            token = tokenManager.getTokenForIMSUser(imsProjectConfig.imsConfig, null)
        }
        return ApolloClient.Builder().serverUrl(imsProjectConfig.imsConfig.graphQLUrl.toString())
            .addHttpHeader("Authorization", "bearer $token").build()
    }

    private suspend fun githubReopenIssue(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        return listOf {
            val client = createClient(imsProjectConfig, userList)
            val response = client.mutation(MutateReopenIssueMutation(issueInfo.githubId)).execute()
            val item = response.data?.closeIssue?.issue?.timelineItems?.nodes?.lastOrNull()
            if (item != null) {
                incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
            }
        }
    }

    private suspend fun githubCloseIssue(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, userList: Iterable<User>
    ): List<suspend () -> Unit> {
        return listOf {
            val client = createClient(imsProjectConfig, userList)
            val response = client.mutation(MutateCloseIssueMutation(issueInfo.githubId)).execute()
            val item = response.data?.closeIssue?.issue?.timelineItems?.nodes?.lastOrNull()
            if (item != null) {
                incoming.handleTimelineEvent(imsProjectConfig, issueInfo, item)
            }
        }
    }

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
        if (relevantTimeline.last() is ReopenedEvent) {
            collectedMutations += githubReopenIssue(imsProjectConfig,
                issueInfo,
                finalBlock.map { it.lastModifiedBy().value })
        }
        if (relevantTimeline.last() is ClosedEvent) {
            collectedMutations += githubCloseIssue(imsProjectConfig,
                issueInfo,
                finalBlock.map { it.lastModifiedBy().value })
        }
        return collectedMutations
    }

    suspend fun issueModified(
        imsProjectConfig: IMSProjectConfig, issue: Issue, issueInfo: IssueInfo
    ): List<suspend () -> Unit> {
        val collectedMutations = mutableListOf<suspend () -> Unit>()
        val timeline = issue.timelineItems().toList().sortedBy { it.lastModifiedAt }
        collectedMutations += pushReopenClose(imsProjectConfig, issueInfo, timeline)
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
        for (mutation in collectedMutations) {
            mutation()
        }
    }
}