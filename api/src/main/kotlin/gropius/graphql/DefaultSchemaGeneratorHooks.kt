package gropius.graphql

import com.expediagroup.graphql.generator.extensions.deepName
import com.expediagroup.graphql.generator.extensions.unwrapType
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.scalars.datetime.DateTimeScalar
import graphql.schema.*
import java.net.URI
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * SchemaGeneratorHooks which support the [TypeGraphQLType] annotation
 * Also adds support for scalars:
 * - [OffsetDateTime] -> DateTime
 * - [URI] -> Url
 * - Duration -> Duration
 * Handles the automatic generation of payload types for mutations annotated with [AutoPayloadType]
 */
object DefaultSchemaGeneratorHooks : SchemaGeneratorHooks {

    /**
     * Code registry used for additional datafetchers
     */
    val codeRegistry = GraphQLCodeRegistry.newCodeRegistry()

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        val typeAnnotation = type.findAnnotation<TypeGraphQLType>()
        return if (typeAnnotation != null) {
            GraphQLTypeReference.typeRef(typeAnnotation.name)
        } else {
            when (type.classifier) {
                OffsetDateTime::class -> DateTimeScalar.INSTANCE
                URI::class -> URLScalar
                Duration::class -> DurationScalar
                else -> null
            }
        }
    }

    override fun didGenerateMutationField(
        kClass: KClass<*>, function: KFunction<*>, fieldDefinition: GraphQLFieldDefinition
    ): GraphQLFieldDefinition {
        return if (function.hasAnnotation<AutoPayloadType>()) {
            val fieldName = fieldDefinition.type.unwrapType().deepName.replaceFirstChar(Char::lowercase)
            val payloadType =
                GraphQLObjectType.newObject().name(fieldDefinition.name.replaceFirstChar(Char::titlecase) + "Payload")
                    .field {
                        it.name(fieldName).description("The result of the mutation").type(fieldDefinition.type)
                    }.build()
            codeRegistry.dataFetcher(
                FieldCoordinates.coordinates(payloadType, fieldName),
                DataFetcher<Any> { it.getSource() })
            fieldDefinition.transform { it.type(payloadType) }
        } else {
            super.didGenerateMutationField(kClass, function, fieldDefinition)
        }
    }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        val oldCodeRegistry = builder.build().codeRegistry
        val newCodeRegistry = oldCodeRegistry.transform {
            it.dataFetchers(codeRegistry.build())
        }
        return builder.codeRegistry(newCodeRegistry)
    }

}