package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.Input

@GraphQLDescription("Input for the addInterfaceSpecificationVersionToComponentVersion mutation")
class AddInterfaceSpecificationVersionToComponentVersionInput(
    @GraphQLDescription("The id of the InterfaceSpecificationVersion to add. Must be part of the same Component as `componentVersion`")
    val interfaceSpecificationVersion: ID,
    @GraphQLDescription("The id of the ComponentVersion to add the InterfaceSpecificationVersion to.")
    val componentVersion: ID,
    @GraphQLDescription("If `true`, the InterfaceSpecificationVersion is added visible")
    val visible: Boolean,
    @GraphQLDescription("If `true`, the InterfaceSpecificationVersion is added invisible")
    val invisible: Boolean
) : Input()