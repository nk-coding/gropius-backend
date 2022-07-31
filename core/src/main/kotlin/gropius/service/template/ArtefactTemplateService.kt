package gropius.service.template

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.template.CreateArtefactTemplateInput
import gropius.model.template.ArtefactTemplate
import gropius.repository.template.ArtefactTemplateRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [ArtefactTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ArtefactTemplateService(
    repository: ArtefactTemplateRepository
) : AbstractTemplateService<ArtefactTemplate, ArtefactTemplateRepository>(repository) {

    /**
     * Creates a new [ArtefactTemplate] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [ArtefactTemplate]
     * @return the saved created [ArtefactTemplate]
     */
    suspend fun createArtefactTemplate(
        authorizationContext: GropiusAuthorizationContext, input: CreateArtefactTemplateInput
    ): ArtefactTemplate {
        input.validate()
        checkCreateTemplatePermission(authorizationContext)
        val template = ArtefactTemplate(input.name, input.description, mutableMapOf(), false)
        createdTemplate(template, input)
        return repository.save(template).awaitSingle()
    }

}