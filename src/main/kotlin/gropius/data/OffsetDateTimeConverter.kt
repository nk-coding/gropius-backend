package gropius.data

import org.neo4j.driver.Value
import org.neo4j.driver.Values
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import java.time.OffsetDateTime

/**
 * Converter for [OffsetDateTime]
 */
class OffsetDateTimeConverter : GenericConverter {
    override fun getConvertibleTypes(): Set<GenericConverter.ConvertiblePair> {
        return setOf(
            GenericConverter.ConvertiblePair(OffsetDateTime::class.java, Value::class.java),
            GenericConverter.ConvertiblePair(Value::class.java, OffsetDateTime::class.java)
        )
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        return if (OffsetDateTime::class.java.isAssignableFrom(sourceType.type)) {
            Values.value(source as OffsetDateTime)
        } else {
            (source as Value).asOffsetDateTime()
        }
    }
}