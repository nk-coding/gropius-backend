package gropius.sync.github

import gropius.model.user.IMSUser
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
     * @param imsUser The IMSUser the token should be for
     * @return token if available
     */
    suspend fun getGithubUserToken(imsUser: IMSUser): String? {
        return System.getenv("GITHUB_DUMMY_PAT")//TODO: @modellbahnfreak!!
    }

    suspend fun getTokenForIMSUser(imsConfig: IMSConfig, imsUser: IMSUser?): String {
        return System.getenv("GITHUB_DUMMY_PAT")//TODO: Login service

        val readUser =
            imsUser ?: neoOperations.findById<IMSUser>(imsConfig.readUser) ?: throw SyncNotificator.NotificatedError(
                "SYNC_GITHUB_USER_NOT_FOUND"
            )
        if (readUser.ims().value != imsConfig.ims) {
            throw SyncNotificator.NotificatedError(
                "SYNC_GITHUB_USER_INVALID_IMS"
            )
        }
        return getGithubUserToken(readUser) ?: throw SyncNotificator.NotificatedError(
            "SYNC_GITHUB_USER_NO_TOKEN"
        )
    }
}