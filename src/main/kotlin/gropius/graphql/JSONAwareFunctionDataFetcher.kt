package gropius.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.server.spring.execution.SpringDataFetcher
import graphql.schema.DataFetchingEnvironment
import io.github.graphglue.graphql.extensions.getGraphQLName
import org.springframework.context.ApplicationContext
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * FunctionDataFetcher which handles files parameter mapping for parameters of GraphQL type JSON correctly
 * Extends [SpringDataFetcher]
 *
 * @param target if present, the object on which the function is invoked
 * @param function the function the data fetcher calls
 * @param applicationContext used to obtain Spring beans
 */
class JSONAwareFunctionDataFetcher(
    target: Any?, function: KFunction<*>, applicationContext: ApplicationContext
) : SpringDataFetcher(target, function, applicationContext) {
    override fun mapParameterToValue(param: KParameter, environment: DataFetchingEnvironment): Pair<KParameter, Any?>? {
        return if (param.findAnnotation<GraphQLType>()?.typeName == "JSON") {
            param to environment.arguments[param.getGraphQLName() ?: param.name]
        } else {
            super.mapParameterToValue(param, environment)
        }
    }
}