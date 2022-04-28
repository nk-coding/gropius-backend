package gropius.model.common

import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty

interface Named {
    @FilterProperty @OrderProperty
    var name: String

    @FilterProperty
    var description: String
}