package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.dto.input.common.CreateExtensibleNodeInput
import io.github.graphglue.model.FilterProperty

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
    @GraphQLDescription("If true InterfaceSpecifications are visible inherited")
    val isVisibleInherited: Boolean,
    @GraphQLDescription("If true InterfaceSpecifications are invisible inherited")
    val isInvisibleInherited: Boolean
) : CreateExtensibleNodeInput()