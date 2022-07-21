package gropius.service.template

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.template.CreateInterfaceSpecificationTemplateInput
import gropius.model.template.InterfacePartTemplate
import gropius.model.template.InterfaceSpecificationTemplate
import gropius.model.template.InterfaceSpecificationVersionTemplate
import gropius.model.template.InterfaceTemplate
import gropius.model.template.ComponentTemplate
import gropius.repository.findAllById
import gropius.repository.template.ComponentTemplateRepository
import gropius.repository.template.InterfaceSpecificationTemplateRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service [InterfaceSpecificationTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param componentTemplateRepository used to get [ComponentTemplate]s
 */
@Service
class InterfaceSpecificationTemplateService(
    repository: InterfaceSpecificationTemplateRepository, val componentTemplateRepository: ComponentTemplateRepository
) : AbstractTemplateService<InterfaceSpecificationTemplate, InterfaceSpecificationTemplateRepository>(repository) {

    /**
     * Creates a new [InterfaceSpecificationTemplate] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [InterfaceSpecificationTemplate]
     * @return the saved created [InterfaceSpecificationTemplate]
     */
    suspend fun createInterfaceSpecificationTemplate(
        authorizationContext: GropiusAuthorizationContext, input: CreateInterfaceSpecificationTemplateInput
    ): InterfaceSpecificationTemplate {
        input.validate()
        checkCreateTemplatePermission(authorizationContext)
        val template = InterfaceSpecificationTemplate(input.name, input.description, mutableMapOf(), false)
        createdTemplate(template, input)
        template.canBeVisibleOnComponents() += componentTemplateRepository.findAllById(input.canBeVisibleOnComponents)
        template.canBeVisibleOnComponents() += template.extends().flatMap { it.canBeVisibleOnComponents() }
        template.canBeInvisibleOnComponents() += componentTemplateRepository.findAllById(input.canBeInvisibleOnComponents)
        template.canBeInvisibleOnComponents() += template.extends().flatMap { it.canBeInvisibleOnComponents() }
        template.interfaceSpecificationVersionTemplate().value =
            createSubTemplate(::InterfaceSpecificationVersionTemplate, input.interfaceSpecificationVersionTemplate)
        template.interfaceTemplate().value = createSubTemplate(::InterfaceTemplate, input.interfaceTemplate)
        template.interfacePartTemplate().value = createSubTemplate(::InterfacePartTemplate, input.interfacePartTemplate)
        return repository.save(template).awaitSingle()
    }

}