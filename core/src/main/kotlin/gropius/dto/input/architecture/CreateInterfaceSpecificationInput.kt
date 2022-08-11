package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createInterfaceSpecification mutation")
class CreateInterfaceSpecificationInput(
    @GraphQLDescription("The id of the Component the created InterfaceSpecification is part of")
    val component: ID
) : InterfaceSpecificationInput()