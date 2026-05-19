package fi.apinkivi.modeler.json.aggregate

import kotlinx.serialization.Serializable

@Serializable
sealed interface PrimitiveNode : NullableNode {
    /** Get the constant value of this node. */
    val constant: Any?

    /** Get the last observed value of this node. */
    val last: Any?
}
