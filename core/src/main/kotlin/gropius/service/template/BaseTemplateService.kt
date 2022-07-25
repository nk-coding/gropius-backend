package gropius.service.template

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.ifPresent
import gropius.dto.input.template.CreateBaseTemplateInput
import gropius.model.template.BaseTemplate
import gropius.model.user.permission.GlobalPermission
import gropius.service.common.NamedNodeService
import io.github.graphglue.authorization.Permission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [BaseTemplate]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class BaseTemplateService<T : BaseTemplate<*, *>, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    NamedNodeService<T, R>(repository) {

    /**
     * Updates [template] based on [input]
     * Sets templateFieldSpecifications based on [input]
     *
     * @param template the [BaseTemplate] to update
     * @param input specifies added templateFieldSpecifications
     */
    fun createdBaseTemplate(template: T, input: CreateBaseTemplateInput) {
        input.templateFieldSpecifications.ifPresent {
            for (field in it) {
                template.templateFieldSpecifications[field.name] = objectMapper.writeValueAsString(field.value)
            }
        }
    }

    /**
     * Checks for CAN_CREATE_TEMPLATE permission on the user provided via [authorizationContext]
     *
     * @param authorizationContext used to get the user to check for CAN_CREATE_TEMPLATE
     * @throws IllegalArgumentException if the permission is not granted
     */
    suspend fun checkCreateTemplatePermission(authorizationContext: GropiusAuthorizationContext) {
        val user = getUser(authorizationContext)
        checkPermission(
            user,
            Permission(GlobalPermission.CAN_CREATE_TEMPLATES, authorizationContext),
            "create/update Templates"
        )
    }
}