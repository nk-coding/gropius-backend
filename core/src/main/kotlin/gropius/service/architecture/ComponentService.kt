package gropius.service.architecture

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.CreateComponentInput
import gropius.model.architecture.Component
import gropius.model.template.ComponentTemplate
import gropius.model.user.permission.GlobalPermission
import gropius.repository.architecture.ComponentRepository
import gropius.repository.findById
import gropius.repository.template.ComponentTemplateRepository
import gropius.service.template.TemplatedNodeService
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [Component]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param templatedNodeService service used to update templatedFields
 * @param componentTemplateRepository used to get [ComponentTemplate]
 */
@Service
class ComponentService(
    repository: ComponentRepository,
    val templatedNodeService: TemplatedNodeService,
    val componentTemplateRepository: ComponentTemplateRepository
) :
    TrackableService<Component, ComponentRepository>(repository) {

    /**
     * Creates a new [Component] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [Component]
     * @return the saved created [Component]
     */
    suspend fun createComponent(
        authorizationContext: GropiusAuthorizationContext,
        input: CreateComponentInput
    ): Component {
        input.validate()
        val user = getUser(authorizationContext)
        checkPermission(
            user,
            Permission(GlobalPermission.CAN_CREATE_COMPONENTS, authorizationContext),
            "User does not have permission to create Components"
        )
        val template = componentTemplateRepository.findById(input.template)
        val templatedFields = templatedNodeService.validateInitialTemplatedFields(template, input)
        val component = Component(input.name, input.description, input.repositoryURL, templatedFields)
        component.template().value = template
        createdExtensibleNode(component, input)
        return repository.save(component).awaitSingle()
    }

}