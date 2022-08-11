package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("Input for the createRelationTemplate mutation")
class CreateRelationTemplateInput(
    @GraphQLDescription("Defines which Relations can use the created Template, at least one RelationCondition has to match")
    val relationConditions: List<RelationConditionInput>
) : CreateTemplateInput() {

    override fun validate() {
        super.validate()
        relationConditions.forEach { it.validate() }
    }
}