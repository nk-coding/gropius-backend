package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.CreateExtensibleNodeInput

@GraphQLDescription("Input to create a RelationCondition")
class RelationConditionInput(
    @GraphQLDescription("Defines which InterfaceSpecifications are inherited via the relation")
    val interfaceSpecificationInheritanceConditions: List<InterfaceSpecificationInheritanceConditionInput>,
    @GraphQLDescription("IDs of Templates of allowed start RelationPartners")
    val from: List<ID>,
    @GraphQLDescription("IDs of Templates of allowed end RelationPartners")
    val to: List<ID>
) : CreateExtensibleNodeInput() {

    override fun validate() {
        super.validate()
        interfaceSpecificationInheritanceConditions.forEach { it.validate() }
    }
}