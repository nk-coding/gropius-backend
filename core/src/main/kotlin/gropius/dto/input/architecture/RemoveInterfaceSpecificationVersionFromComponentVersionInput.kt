package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.Input

@GraphQLDescription("Input for the removeInterfaceSpecificationVersionfromComponentVersion mutation")
class RemoveInterfaceSpecificationVersionFromComponentVersionInput(
    @GraphQLDescription("The id of the InterfaceSpecificationVersion to remove. Must be part of the same Component as `componentVersion`")
    val interfaceSpecificationVersion: ID,
    @GraphQLDescription("The id of the ComponentVersion to remove the InterfaceSpecificationVersion from.")
    val componentVersion: ID,
    @GraphQLDescription("If `true`, interfaceSpecificationVersion will be no longer visible on componentVersion")
    val visible: Boolean,
    @GraphQLDescription("If `true`, interfaceSpecificationVersion will be no longer invisible on componentVersion")
    val invisible: Boolean
) : Input()