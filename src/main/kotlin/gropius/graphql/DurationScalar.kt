package gropius.graphql

import graphql.language.StringValue
import graphql.schema.*
import java.time.Duration
import java.time.OffsetDateTime

private val DurationCoercing = object : Coercing<Duration, String> {
    override fun serialize(input: Any): String {
        if (input !is OffsetDateTime) {
            throw CoercingSerializeException("Expected Duration")
        }
        return input.toString()
    }

    override fun parseValue(input: Any): Duration {
        if (input !is Duration) {
            throw CoercingParseValueException("Expected Duration")
        }
        return input
    }

    override fun parseLiteral(input: Any): Duration {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue'")
        }
        return Duration.parse(input.value)
    }
}

val DurationScalar: GraphQLScalarType = GraphQLScalarType.newScalar()
    .name("Duration")
    .description("An ISO 8601 duration string, e.g. ")
    .coercing(DurationCoercing)
    .build()