package gropius.graphql

import graphql.scalars.datetime.DateTimeScalar
import io.github.graphglue.connection.filter.definition.scalars.ComparableFilterDefinition
import java.time.OffsetDateTime

/**
 * Filter definition for a [OffsetDateTime] property
 *
 * @param name the name of the field in the filter
 * @param neo4jName the name of the property in the database
 */
class DateTimeFilterDefinition(name: String, neo4jName: String) : ComparableFilterDefinition(
    name,
    "Filter which can be used to filter for Nodes with a specific DateTime field",
    "DateTimeFilterInput",
    DateTimeScalar.INSTANCE,
    neo4jName,
    emptyList()
)