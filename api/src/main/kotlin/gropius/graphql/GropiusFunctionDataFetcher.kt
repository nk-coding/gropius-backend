package gropius.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.spring.execution.SpringDataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.context.ApplicationContext
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubtypeOf

/**
 * FunctionDataFetcher which handles files parameter mapping for parameters of GraphQL type JSON correctly
 * Allows input classes to have `lateinit` properties which are correctly deserialized
 * Extends [SpringDataFetcher]
 *
 * @param target if present, the object on which the function is invoked
 * @param function the function the data fetcher calls
 * @param applicationContext used to obtain Spring beans
 */
class GropiusFunctionDataFetcher(
    target: Any?, function: KFunction<*>, applicationContext: ApplicationContext
) : SpringDataFetcher(target, function, applicationContext) {
    override fun mapParameterToValue(param: KParameter, environment: DataFetchingEnvironment): Pair<KParameter, Any?>? {
        return when {
            param.hasAnnotation<GraphQLIgnore>() -> super.mapParameterToValue(param, environment)
            param.type.isSubtypeOf(DataFetchingEnvironment::class.createType()) -> param to environment
            else -> param to convertArgumentValueFromParam(param, environment.arguments)
        }
    }
}