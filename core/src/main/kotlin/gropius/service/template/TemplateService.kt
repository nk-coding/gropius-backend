package gropius.service.template

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.template.UpdateTemplateDeprecationStatusInput
import gropius.model.template.Template
import gropius.model.user.permission.GlobalPermission
import gropius.repository.findById
import gropius.repository.template.TemplateRepository
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [Template]s. Provides function to update the deprecation status
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class TemplateService(repository: TemplateRepository) :
    BaseTemplateService<Template<*, *>, TemplateRepository>(repository) {

    /**
     * Gets the [Template] based on the provided [input], and updates the deprecation status accordingly
     * Checks the authorization status
     *
     * @param authorizationContext used to get the user & to check for the required permission
     * @param input specifies which Template to update and how to change the deprecation status
     * @return the updated saved [Template]
     */
    suspend fun updateTemplateDeprecationStatus(
        authorizationContext: GropiusAuthorizationContext,
        input: UpdateTemplateDeprecationStatusInput
    ): Template<*, *> {
        input.validate()
        val template = repository.findById(input.id)
        checkCreateTemplatePermission(authorizationContext)
        template.isDeprecated = input.isDeprecated
        return repository.save(template).awaitSingle()
    }
}