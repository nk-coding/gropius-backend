package gropius

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Configuration properties for the internal API
 *
 * @param apiToken if provided, all requests to the graphql api must provide the token in the Authorization header
 */
@ConstructorBinding
@ConfigurationProperties("gropius.api.internal")
data class GropiusInternalApiConfigurationProperties( val apiToken: String? = null)