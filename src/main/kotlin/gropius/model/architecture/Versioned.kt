package gropius.model.architecture

import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty

interface Versioned {
    @FilterProperty
    @OrderProperty
    var version: String
}