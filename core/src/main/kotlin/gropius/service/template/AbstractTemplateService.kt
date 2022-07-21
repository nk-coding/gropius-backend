package gropius.service.template

import gropius.dto.input.orElse
import gropius.dto.input.template.CreateTemplateInput
import gropius.dto.input.template.SubTemplateInput
import gropius.model.template.SubTemplate
import gropius.model.template.Template
import gropius.repository.findAllById
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Template]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class AbstractTemplateService<T : Template<*, T>, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    BaseTemplateService<T, R>(repository) {

    /**
     * Updates [template] based on [input]
     * Sets extends and templateFieldSpecifications
     *
     * @param template the [Template] to update
     * @param input specifies templateFieldSpecifications, extended templates
     */
    suspend fun createdTemplate(template: T, input: CreateTemplateInput) {
        createdBaseTemplate(template, input)
        val extendedTemplates = repository.findAllById(input.extends.orElse(emptyList()))
        template.extends().addAll(extendedTemplates)
        val inheritedFields = extendedTemplates.flatMap { it.templateFieldSpecifications.entries }.map { it.toPair() }
        val allFields = inheritedFields + template.templateFieldSpecifications.entries.map { it.toPair() }
        val duplicates = allFields.groupingBy { it.first }.eachCount().filter { it.value > 1 }.keys
        if (duplicates.isNotEmpty()) {
            throw IllegalStateException("Duplicate names found: $duplicates")
        }
        for ((name, value) in inheritedFields) {
            template.templateFieldSpecifications[name] = value
        }
    }

    /**
     * Creates a [SubTemplate] based on the provided [constructor] and [input]
     * Does not check authorization status and does not save the created [SubTemplate]
     *
     * @param T the specific subtype of [SubTemplate] to create
     * @param constructor function used to create the [SubTemplate] subtype instance, usually the constructor
     * @param input used to get name, description and templateFieldSpecifications
     * @return the created [SubTemplate]
     */
    fun <T : SubTemplate<*, *, *>> createSubTemplate(
        constructor: (String, String, MutableMap<String, String>) -> T, input: SubTemplateInput
    ): T {
        val fields = input.extensionFields.orElse(emptyList()).associate {
            it.name to objectMapper.writeValueAsString(it.value)
        }.toMutableMap()
        return constructor(input.name, input.description, fields)
    }

}