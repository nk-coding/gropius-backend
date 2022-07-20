package gropius.schema.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import gropius.authorization.gropiusAuthorizationContext
import gropius.dto.input.template.CreateComponentTemplateInput
import gropius.dto.input.template.UpdateTemplateDeprecationStatusInput
import gropius.graphql.AutoPayloadType
import gropius.model.template.ComponentTemplate
import gropius.model.template.Template
import gropius.service.template.TemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TemplateMutations() : Mutation {

    @GraphQLDescription("Updates the deprecation state of the template, requires CAN_CREATE_TEMPLATES")
    @AutoPayloadType
    suspend fun updateTemplateDeprecationStatus(
        @GraphQLDescription("Defines the new deprecation status and the Template to update")
        input: UpdateTemplateDeprecationStatusInput,
        dfe: DataFetchingEnvironment,
        @GraphQLIgnore
        @Autowired
        templateService: TemplateService
    ): Template<*, *> {
        return templateService.updateTemplateDeprecationStatus(dfe.gropiusAuthorizationContext, input)
    }

}