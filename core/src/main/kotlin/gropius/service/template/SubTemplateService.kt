package gropius.service.template

import gropius.dto.input.template.SubTemplateInput
import gropius.model.template.SubTemplate
import gropius.repository.template.SubTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service for [SubTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class SubTemplateService(
    repository: SubTemplateRepository
) : BaseTemplateService<SubTemplate<*, *, *>, SubTemplateRepository>(repository) {

    /**
     * Creates a [SubTemplate] based on the provided [constructor] and [input]
     * Does not check authorization status and does not save the created [SubTemplate]
     *
     * @param T the specific subtype of [SubTemplate] to create
     * @param constructor function used to create the [SubTemplate] subtype instance, usually the constructor
     * @param input used to get name, description and templateFieldSpecifications
     * @param extendedTemplates
     * @return the created [SubTemplate]
     */
    fun <T : SubTemplate<*, *, *>> createSubTemplate(
        constructor: (String, String, MutableMap<String, String>) -> T,
        input: SubTemplateInput,
        extendedTemplates: Collection<T>
    ): T {
        val template = constructor(input.name, input.description, mutableMapOf())
        createdBaseTemplate(template, input, extendedTemplates)
        return template
    }

}