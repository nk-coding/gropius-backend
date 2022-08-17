package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.dto.input.common.CreateExtensibleNodeInput

@GraphQLDescription("Input to create a InterfaceSpecificationDerivationCondition")
class InterfaceSpecificationDerivationConditionInput(
    @GraphQLDescription("If true, visible self-defined InterfaceSpecifications are derived")
    val derivesVisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, invisible self-defined InterfaceSpecifications are derived")
    val derivesInvisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, visible derived InterfaceSpecifications are derived")
    val derivesVisibleDerived: Boolean,
    @GraphQLDescription("If true, invisible derived InterfaceSpecifications are derived")
    val derivesInvisibleDerived: Boolean,
    @GraphQLDescription("If true InterfaceSpecifications are visible derived")
    val isVisibleDerived: Boolean,
    @GraphQLDescription("If true InterfaceSpecifications are invisible derived")
    val isInvisibleDerived: Boolean
) : CreateExtensibleNodeInput()