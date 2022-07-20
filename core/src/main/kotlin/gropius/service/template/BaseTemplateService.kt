package gropius.service.template

import gropius.dto.input.ifPresent
import gropius.dto.input.template.CreateBaseTemplateInput
import gropius.model.template.BaseTemplate
import gropius.service.common.NamedNodeService
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [BaseTemplate]
 *
 * @param repository the associated repository used for CRUD functionality
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
    suspend fun createdBaseTemplate(template: T, input: CreateBaseTemplateInput) {
        input.extensionFields.ifPresent {
            for (field in it) {
                template.templateFieldSpecifications[field.name] = objectMapper.writeValueAsString(field.value)
            }
        }
    }
}