package gropius.service.template

import gropius.dto.input.template.CreateTemplateInput
import gropius.model.template.RelationPartnerTemplate
import gropius.repository.GropiusRepository

/**
 * Base class for services for subclasses of [RelationPartnerTemplate]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class RelationPartnerTemplateService<T : RelationPartnerTemplate<*, T>, R : GropiusRepository<T, String>>(
    repository: R
) : AbstractTemplateService<T, R>(repository) {

    /**
     * Updates [template] based on [input]
     * Calls [createdTemplate]
     * Sets the [RelationPartnerTemplate.possibleStartOfRelations] and [RelationPartnerTemplate.possibleEndOfRelations]
     * based on extended Templates
     *
     * @param template the [RelationPartnerTemplate] to update
     * @param input specifies added templateFieldSpecifications
     */
    suspend fun createdRelationPartnerTemplate(template: T, input: CreateTemplateInput) {
        createdTemplate(template, input)
        template.possibleStartOfRelations() += template.extends().flatMap { it.possibleStartOfRelations() }
        template.possibleEndOfRelations() += template.extends().flatMap { it.possibleEndOfRelations() }
    }

}