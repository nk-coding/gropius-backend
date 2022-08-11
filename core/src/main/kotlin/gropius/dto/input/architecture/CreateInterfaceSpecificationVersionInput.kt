package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createInterfaceSpecificationVersion mutation")
class CreateInterfaceSpecificationVersionInput(
    @GraphQLDescription("The id of the InterfaceSpecification the created InterfaceSpecificationVersion is part of")
    val interfaceSpecification: ID
) : InterfaceSpecificationVersionInput()