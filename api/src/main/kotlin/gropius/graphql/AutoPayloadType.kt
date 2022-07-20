package gropius.graphql

/**
 * Can be applied to GraphQL mutation functions
 * Results in automatically generated payload return types
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class AutoPayloadType
