package gropius.dto.input

import com.expediagroup.graphql.generator.execution.OptionalInput

/**
 * Executes [block] with the value if `this is OptionalInput.Defined`
 *
 * @param block executed if input is defined
 */
inline fun <T> OptionalInput<T>.ifPresent(block: (T) -> Unit) {
    if (this is OptionalInput.Defined) {
        block(this.value!!)
    }
}

/**
 * `true` if the input is present
 */
val OptionalInput<*>.isPresent: Boolean get() = this is OptionalInput.Defined

/**
 * If present, returns its value, otherwise [value]
 *
 * @param value result in case not present
 * @return its value if present, otherwise [value]
 */
fun <T> OptionalInput<T>.orElse(value: T): T {
    ifPresent {
        return it
    }
    return value
}