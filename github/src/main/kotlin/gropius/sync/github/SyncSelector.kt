package gropius.sync.github

import com.apollographql.apollo3.ApolloClient
import gropius.repository.architecture.IMSIssueRepository
import gropius.sync.IssueCleaner
import gropius.sync.JsonHelper
import gropius.sync.SyncNotificator
import gropius.sync.github.config.IMSConfig
import gropius.sync.github.config.IMSConfigManager
import gropius.sync.github.config.IMSProjectConfig
import gropius.sync.github.repository.IssueInfoRepository
import gropius.sync.github.repository.RepositoryInfoRepository
import gropius.sync.github.repository.TimelineEventInfoRepository
import gropius.sync.github.utils.TimelineItemHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.stereotype.Component

/**
 * Stateless component for the management part of the sync
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
 * @param imsIssueRepository Reference for the spring instance of IMSIssueRepository
 * @param incoming Reference for the spring instance of Incoming
 * @param outgoing Reference for the spring instance of Outgoing
 */
@Component
class SyncSelector(
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
    private val tokenManager: TokenManager,
    private val imsIssueRepository: IMSIssueRepository,
    private val incoming: Incoming,
    private val outgoing: Outgoing
) {
    /**
     * Logger used to print notifications
     */
    private val logger = LoggerFactory.getLogger(SyncSelector::class.java)

    /**
     * Sync GitHub to Gropius
     */
    suspend fun sync() {
        logger.info("Sync started")
        imsConfigManager.findTemplates()
        for (imsTemplate in imsConfigManager.findTemplates()) {
            for (ims in imsTemplate.usedIn()) {
                try {
                    val imsConfig = IMSConfig(helper, ims, imsTemplate)
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
        logger.info("Sync exited without exception")
    }

    /**
     * Sync one IMS
     * @param imsConfig the config of the IMS
     */
    private suspend fun syncIMS(imsConfig: IMSConfig) {
        val token = tokenManager.getTokenForIMSUser(imsConfig.ims, imsConfig.readUser, null)
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
        incoming.syncIssues(imsProjectConfig, apolloClient)
        outgoing.syncIssues(imsProjectConfig)
    }

    /**
     * Sync one IMSProject
     * @param imsProjectConfig the config of the IMSProject
     * @param apolloClient the client to use4 for grpahql queries
     */
    private suspend fun syncProject(imsProjectConfig: IMSProjectConfig, apolloClient: ApolloClient) {
        syncIssues(imsProjectConfig, apolloClient)
    }
}