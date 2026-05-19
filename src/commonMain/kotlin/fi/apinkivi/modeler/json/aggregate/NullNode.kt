package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("null")
data class NullNode(
    override var nullCount: Int = 0,
) : PrimitiveNode {
    override val constant get() = null

    override val last get() = null

    override val nodeTypeSymbol get() = "🕳️" // ☁️ 🕳️ ∅ ␀

    override val valueString get() = VALUE

    override val view get() = "null"

    /** All nodes are nullable, so it is safe to discard this object. */
    override fun promote(data: Data): Node {
        logger.info { "$log $nullCount nulls -> $data" }
        return data.node.also { it.setObservedNulls(nullCount) }
    }

    override fun toString() = view

    override fun symbol(value: Any?) = VALUE

    companion object {
        /** Value symbol for null. */
        const val VALUE = "␀"
    }
}
