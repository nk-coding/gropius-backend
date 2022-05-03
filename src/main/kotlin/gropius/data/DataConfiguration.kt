package gropius.data

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.convert.Neo4jConversions
import java.time.OffsetDateTime

/**
 * Configuration for the Neo4j database
 */
@Configuration
class DataConfiguration {

    /**
     * Provides the converts
     * Includes a converter for [OffsetDateTime]
     *
     * @return the coverter set
     */
    @Bean
    fun neo4jConversion(): Neo4jConversions {
        return Neo4jConversions(setOf(OffsetDateTimeConverter()))
    }

}