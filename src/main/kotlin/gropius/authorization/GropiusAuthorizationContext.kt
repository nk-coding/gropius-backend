package gropius.authorization

import gropius.model.user.GropiusUser
import io.github.graphglue.authorization.AuthorizationContext
import org.neo4j.cypherdsl.core.Cypher

/**
 * Default authorization context
 *
 * @param userId the id of the authenticated [GropiusUser]
 */
class GropiusAuthorizationContext(userId: String) : AuthorizationContext {

    /**
     * A Cypher DSL parameter with the userId
     */
    val useridParameter = Cypher.anonParameter(userId)

}