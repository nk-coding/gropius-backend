package gropius.sync.github

import com.apollographql.apollo3.ApolloClient
import gropius.model.issue.Issue
import gropius.sync.IssueCleaner
import gropius.sync.github.generated.fragment.IssueDataExtensive
import gropius.sync.github.generated.fragment.TimelineItemData
import gropius.sync.github.generated.fragment.TimelineItemData.Companion.asIssueComment
import gropius.sync.github.generated.fragment.TimelineItemData.Companion.asNode
import gropius.sync.github.model.IssueInfo
import gropius.sync.github.model.TimelineEventInfo
import gropius.sync.github.repository.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findById
import org.springframework.stereotype.Component
import java.lang.Exception
import java.time.OffsetDateTime

/**
 * Stateless component for the incoming part of the sync
 * @param helper Reference for the spring instance of JsonHelper
 * @param imsConfigManager Reference for the spring instance of IMSConfigManager
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 * @param syncNotificator Reference for the spring instance of SyncNotificator
 * @param tokenManager Reference for the spring instance of TokenManager
 * @param repositoryInfoRepository Reference for the spring instance of RepositoryInfoRepository
 * @param issueInfoRepository Reference for the spring instance of IssueInfoRepository
 * @param mongoOperations Reference for the spring instance of ReactiveMongoOperations
 * @param timelineEventInfoRepository Reference for the spring instance of TimelineEventInfoRepository
 * @param issueCleaner Reference for the spring instance of IssueCleaner
 * @param nodeSourcerer Reference for the spring instance of NodeSourcerer
 * @param timelineItemHandler Reference for the spring instance of TimelineItemHandler
 */
@Component
class Incoming(
    private val repositoryInfoRepository: RepositoryInfoRepository,
    private val issueInfoRepository: IssueInfoRepository,
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
    private val tokenManager: TokenManager
) {

    /**
     * Logger used to print notifications
     */
    private val logger = LoggerFactory.getLogger(Incoming::class.java)

    /**
     * Mark issue as dirty
     * @param info The full dataset of an issue
     * @param imsProjectConfig Config of the active project
     * @return The DateTime the issue was last changed
     */
    private suspend fun issueModified(imsProjectConfig: IMSProjectConfig, info: IssueDataExtensive): OffsetDateTime {
        val (_, mongoIssue) = nodeSourcerer.ensureIssue(imsProjectConfig, info)
        mongoOperations.updateFirst(
            Query(where("_id").`is`(mongoIssue.id!!)), Update().set(IssueInfo::dirty.name, true), IssueInfo::class.java
        ).awaitSingle()
        return info.updatedAt
    }

    /**
     * Save a single timeline event into the database
     * @param issueInfo the issue the timeline belongs to
     * @param event a single timeline item
     * @param imsProjectConfig Config of the active project
     * @return The time of the event or null for error
     */
    @Suppress("UNUSED_VALUE")
    private suspend fun handleTimelineEvent(
        imsProjectConfig: IMSProjectConfig, issueInfo: IssueInfo, event: TimelineItemData
    ): OffsetDateTime? {
        val dbEntry = timelineEventInfoRepository.findByUrlAndGithubId(imsProjectConfig.url, event.asNode()!!.id)
        return if (event.asIssueComment() != null) {
            val (neoId, time) = timelineItemHandler.handleIssueComment(
                imsProjectConfig, issueInfo, event.asIssueComment()!!, dbEntry?.neo4jId
            )
            if (time != null) {
                timelineEventInfoRepository.save(
                    TimelineEventInfo(
                        event.asNode()!!.id, neoId, time, event.__typename, imsProjectConfig.url
                    )
                ).awaitSingle()
                var issue = neoOperations.findById<Issue>(issueInfo.neo4jId)!!
                issue.lastUpdatedAt = maxOf(issue.lastUpdatedAt, time)
                issue = neoOperations.save(issue).awaitSingle()
            }
            time
        } else if (dbEntry != null) {
            dbEntry.lastModifiedAt
        } else {
            val (neoId, time) = timelineItemHandler.handleIssueModifiedItem(imsProjectConfig, issueInfo, event)
            if (time != null) {
                timelineEventInfoRepository.save(
                    TimelineEventInfo(
                        event.asNode()!!.id, neoId, time, event.__typename, imsProjectConfig.url
                    )
                ).awaitSingle()
                var issue = neoOperations.findById<Issue>(issueInfo.neo4jId)!!
                issue.lastUpdatedAt = maxOf(issue.lastUpdatedAt, time)
                issue = neoOperations.save(issue).awaitSingle()
            }
            time
        }
    }

    /**
     * Sync GitHub to gropius
     */
    suspend fun sync() {
        imsConfigManager.findTemplates()
        for (imsTemplate in imsConfigManager.findTemplates()) {
            for (ims in imsTemplate.usedIn()) {
                try {
                    val imsConfig = IMSConfig(helper, ims)
                    syncIMS(imsConfig)
                } catch (e: SyncNotificator.NotificatedError) {
                    syncNotificator.sendNotification(
                        ims, SyncNotificator.NotificationDummy(e)
                    )
                } catch (e: Exception) {
                    logger.warn("Error in global sync", e)
                }
            }
        }
    }

    /**
     * Sync one IMS
     * @param imsConfig the config of the IMS
     */
    private suspend fun syncIMS(imsConfig: IMSConfig) {
        val token = tokenManager.getTokenForUser(imsConfig, null)
        val apolloClient = ApolloClient.Builder().serverUrl(imsConfig.graphQLUrl.toString())
            .addHttpHeader("Authorization", "bearer $token").build()
        for (project in imsConfig.ims.projects()) {
            try {
                val imsProjectConfig = IMSProjectConfig(helper, imsConfig, project)
                syncProject(imsProjectConfig, apolloClient)
            } catch (e: SyncNotificator.NotificatedError) {
                syncNotificator.sendNotification(
                    project, SyncNotificator.NotificationDummy(e)
                )
            } catch (e: Exception) {
                logger.warn("Error in IMS sync", e)
            }
        }
    }

    /**
     * Sync issues of one IMSProject
     * @param imsProjectConfig the config of the IMSProject
     * @param apolloClient the client to use4 for grpahql queries
     */
    private suspend fun syncIssues(imsProjectConfig: IMSProjectConfig, apolloClient: ApolloClient) {
        val issueGrabber = IssueGrabber(
            imsProjectConfig.repo, repositoryInfoRepository, mongoOperations, apolloClient, imsProjectConfig
        )
        issueGrabber.requestNewNodes()
        issueGrabber.iterate {
            issueModified(imsProjectConfig, it)
        }
        for (issue in issueInfoRepository.findByUrlAndDirtyIsTrue(imsProjectConfig.url).toList()) {
            val timelineGrabber = TimelineGrabber(
                issueInfoRepository, mongoOperations, issue.githubId, apolloClient, imsProjectConfig
            )
            timelineGrabber.requestNewNodes()
            try {
                val errorInserting = timelineGrabber.iterate {
                    handleTimelineEvent(imsProjectConfig, issue, it)
                }
                issueCleaner.cleanIssue(issue.neo4jId)
                if (!errorInserting) {
                    logger.info("Finished issue: " + issue.id!!.toHexString())
                    mongoOperations.updateFirst(
                        Query(where("_id").`is`(issue.id)),
                        Update().set(IssueInfo::dirty.name, false),
                        IssueInfo::class.java
                    ).awaitSingle()
                }
            } catch (e: SyncNotificator.NotificatedError) {
                TODO("Create IMSIssue")/*
                syncNotificator.sendNotification(
                    imsIssue, SyncNotificator.NotificationDummy(e)
                )
                */
            } catch (e: Exception) {
                logger.warn("Error in issue sync", e)
            }
        }
    }

    /**
     * Sync one IMSProject
     * @param imsProjectConfig the config of the IMSProject
     * @param apolloClient the client to use4 for grpahql queries
     */
    private suspend fun syncProject(imsProjectConfig: IMSProjectConfig, apolloClient: ApolloClient) {
        val issueGrabber = IssueGrabber(
            imsProjectConfig.repo, repositoryInfoRepository, mongoOperations, apolloClient, imsProjectConfig
        )
        issueGrabber.requestNewNodes()
        issueGrabber.iterate {
            issueModified(imsProjectConfig, it)
        }
        for (issue in issueInfoRepository.findByUrlAndDirtyIsTrue(imsProjectConfig.url).toList()) {
            val timelineGrabber = TimelineGrabber(
                issueInfoRepository, mongoOperations, issue.githubId, apolloClient, imsProjectConfig
            )
            timelineGrabber.requestNewNodes()
        }
        syncIssues(imsProjectConfig, apolloClient)
    }
}
