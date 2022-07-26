package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createInterfacePart mutation")
class CreateInterfacePartInput(
    @GraphQLDescription("The id of the InterfaceSpecification the created InterfacePart is part of")
    val interfaceSpecification: ID
) : InterfacePartInput()