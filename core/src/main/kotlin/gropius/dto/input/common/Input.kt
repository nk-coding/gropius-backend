package gropius.dto.input.common

/**
 * Base class for all input classes that require validation
 */
abstract class Input {

    /**
     * Validates this input
     *
     * @throws IllegalArgumentException if invalid
     */
    open fun validate() {}

}