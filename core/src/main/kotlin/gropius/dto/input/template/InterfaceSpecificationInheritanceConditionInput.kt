package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.dto.input.common.CreateExtensibleNodeInput

@GraphQLDescription("Input to create a InterfaceSpecificationInheritanceCondition")
class InterfaceSpecificationInheritanceConditionInput(
    @GraphQLDescription("If true, visible self-defined InterfaceSpecifications are inherited")
    val inheritsVisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, invisible self-defined InterfaceSpecifications are inherited")
    val inheritsInvisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, visible derived InterfaceSpecifications are inherited")
    val inheritsVisibleDerived: Boolean,
    @GraphQLDescription("If true, invisible derived InterfaceSpecifications are inherited")
    val inheritsInvisibleDerived: Boolean,
    @GraphQLDescription(
        """If true, inherited InterfaceSpecifications are visible
         on the end of the Relation, otherwise invisible
        """
    )
    val isVisibleInherited: Boolean
) : CreateExtensibleNodeInput()