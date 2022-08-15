package gropius.sync.github

import com.fasterxml.jackson.annotation.JsonProperty
import gropius.model.architecture.IMS
import gropius.model.architecture.IMSIssue
import gropius.model.architecture.IMSProject
import gropius.model.common.ExtensibleNode
import gropius.model.template.MutableTemplatedNode
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findById
import org.springframework.stereotype.Component

/**
 * Dummy class for the future notification center
 * @param helper Reference for the spring instance of JsonHelper
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 */
@Component
class SyncNotificator(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations, private val helper: JsonHelper
) {
    /**
     * Logger used to print notifications
     */
    private val logger = LoggerFactory.getLogger(SyncNotificator::class.java)

    class NotificatedError(val code: String, vararg args: String) : Exception(code) {
        val args = args.asList()
    }

    data class NotificationDummy(
        @JsonProperty("title")
        val title: String,
        @JsonProperty("content")
        val content: String
    ) {
        constructor(error: NotificatedError) : this("Sync Error", error.code + ": " + error.args.joinToString(", ")) {}
    }

    private suspend fun sendNotification(node: ExtensibleNode, dummy: NotificationDummy) {
        logger.info("Send Notification: $dummy")
        if (node is MutableTemplatedNode) {
            node.templatedFields["last-notification"] = helper.objectMapper.writeValueAsString(dummy)
            neoOperations.save(node).awaitSingle()
        }
    }

    suspend fun sendNotification(ims: IMS, dummy: NotificationDummy) {
        sendNotification(ims as ExtensibleNode, dummy)
    }

    suspend fun sendNotification(imsProject: IMSProject, dummy: NotificationDummy) {
        sendNotification(imsProject as ExtensibleNode, dummy)
    }

    suspend fun sendNotification(imsIssue: IMSIssue, dummy: NotificationDummy) {
        sendNotification(imsIssue as ExtensibleNode, dummy)
    }
}