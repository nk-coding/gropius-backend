package gropius.sync.github

import gropius.model.architecture.IMS
import gropius.model.user.GropiusUser
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findById
import org.springframework.stereotype.Component

/**
 * Manager for token from login service
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 */
@Component
class TokenManager(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations
) {

    /**
     * Request an user token from the auth service
     * @param ims IMS the token is requested for
     * @param gropiusUser The user the token should be for
     * @return token if available
     */
    suspend fun getGithubUserToken(ims: IMS, gropiusUser: GropiusUser): String? {
        return System.getenv("GITHUB_DUMMY_PAT")//TODO: @modellbahnfreak!!
    }

    suspend fun getTokenForUser(imsConfig: IMSConfig, gropiusUser: GropiusUser?): String {
        val readUser = gropiusUser ?: neoOperations.findById<GropiusUser>(imsConfig.readUser)
        ?: throw SyncNotificator.NotificatedError(
            "SYNC_GITHUB_USER_NOT_FOUND"
        )
        return getGithubUserToken(imsConfig.ims, readUser) ?: throw SyncNotificator.NotificatedError(
            "SYNC_GITHUB_USER_NO_TOKEN"
        )
    }
}