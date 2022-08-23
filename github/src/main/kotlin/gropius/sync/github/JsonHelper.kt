package gropius.sync.github

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * Spring component containing helper function for github sync related json operations
 * @param objectMapper Reference for the spring instance of ObjectMapper
 */
@Component
class JsonHelper(
    val objectMapper: ObjectMapper
) {
    /**
     * Parse a json value into a string
     * @param input the json value stringified
     * @return null if null or not a valid json string
     */
    fun parseString(input: String?): String? {
        if (input == null) {
            return null
        }
        return objectMapper.readTree(input).textValue()
    }
}