package gropius.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLType

/**
 * Annotation like [GraphQLType], except that the type and not the property / parameter is annotated
 * Can be used e.g. to annotate the Type parameter of a List.
 *
 * @param name the name of the GraphQL type
 */
@MustBeDocumented
@Target(AnnotationTarget.TYPE)
annotation class TypeGraphQLType(val name: String)
