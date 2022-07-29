package gropius.sync.github

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonHelper(
    val objectMapper: ObjectMapper
) {
    fun parseString(input: String?): String? {
        if (input == null)
            return null;
        return objectMapper.readTree(input).textValue()
    }
}