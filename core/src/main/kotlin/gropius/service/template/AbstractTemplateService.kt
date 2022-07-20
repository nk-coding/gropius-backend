package gropius.service.template

import gropius.dto.input.orElse
import gropius.dto.input.template.CreateTemplateInput
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

}