package gropius.sync.github

import gropius.sync.github.generated.fragment.MetaData
import gropius.sync.github.generated.fragment.PageInfoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.time.OffsetDateTime

abstract class Grabber<T : Any> {

    interface StepResponse<T : Any> {
        val metaData: MetaData
        val nodes: Iterable<T>
        val totalCount: Int
        val pageInfoData: PageInfoData
    }

    protected abstract suspend fun grabStep(since: OffsetDateTime?, cursor: String?, count: Int): StepResponse<T>?
    protected abstract suspend fun writeTimestamp(time: OffsetDateTime)
    protected abstract suspend fun readTimestamp(): OffsetDateTime?
    protected abstract suspend fun addToCache(node: T): ObjectId
    protected abstract suspend fun iterateCache(): Flow<T>
    protected abstract suspend fun removeFromCache(node: String)
    protected abstract suspend fun increasedFailedCache(node: String)
    protected abstract fun nodeId(node: T): String

    private suspend fun handleStepResponse(response: Grabber.StepResponse<T>) {
        for (node in response.nodes) {
            addToCache(node)
        }
    }

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