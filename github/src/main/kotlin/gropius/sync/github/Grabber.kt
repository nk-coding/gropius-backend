package gropius.sync.github

import gropius.sync.github.generated.fragment.MetaData
import gropius.sync.github.generated.fragment.PageInfoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.time.OffsetDateTime

/**
 * Requests data from github using steps that are managed over restarts for processing items (e.g. issues, timeline, ...)
 */
abstract class Grabber<T : Any> {

    /**
     * The response of a single step
     */
    interface StepResponse<T : Any> {
        val metaData: MetaData
        val nodes: Iterable<T>
        val totalCount: Int
        val pageInfoData: PageInfoData
    }

    /**
     * Request a single step from github
     */
    protected abstract suspend fun grabStep(since: OffsetDateTime?, cursor: String?, count: Int): StepResponse<T>?

    /**
     * Set the newest timestamp to the given timestamp or newer
     */
    protected abstract suspend fun writeTimestamp(time: OffsetDateTime)

    /**
     * Read the highest timestamp from the database
     */
    protected abstract suspend fun readTimestamp(): OffsetDateTime?

    /**
     * Add a node into the database cache
     */
    protected abstract suspend fun addToCache(node: T): ObjectId

    /**
     * Iterate through all nodes of the database cache
     */
    protected abstract suspend fun iterateCache(): Flow<T>

    /**
     * Remove a single id from the database cache
     */
    protected abstract suspend fun removeFromCache(node: String)

    /**
     * Increase the number of failed attempts for a node in the cache
     */
    protected abstract suspend fun increasedFailedCache(node: String)

    /**
     * Get the id from a node
     */
    protected abstract fun nodeId(node: T): String

    private suspend fun handleStepResponse(response: Grabber.StepResponse<T>) {
        for (node in response.nodes) {
            addToCache(node)
        }
    }

    /**
     * Update nodes with fresh data
     */
    suspend fun requestNewNodes() {
        var githubCursor: String? = null
        var remaining = 1
        do {
            println("Stepping " + (remaining.coerceIn(1, 100)) + " nodes from " + githubCursor)
            val response = grabStep(readTimestamp(), githubCursor, remaining.coerceIn(1, 100))
            if (response != null) {
                if (githubCursor == null) {
                    remaining = response.totalCount //TODO: Should be subtracted with the number of known nodes
                    println("Requesting $remaining nodes")
                }
                githubCursor = response.pageInfoData?.endCursor
                handleStepResponse(response);
            }
        } while ((githubCursor != null) && (remaining > 0))
    }

    /**
     * Iterate through all nodes that have not yet been done
     */
    suspend fun iterate(callback: suspend (atom: T) -> OffsetDateTime?) {
        val times = mutableListOf<OffsetDateTime>()
        for (node in iterateCache().toList()) {
            increasedFailedCache(nodeId(node))
            println(node)
            val newMaxTime = callback(node)
            if (newMaxTime != null) {
                times.add(newMaxTime)
                removeFromCache(nodeId(node))
            }
        }
        if (times.size > 0) {
            writeTimestamp(times.maxOrNull()!!)
        }
    }
}