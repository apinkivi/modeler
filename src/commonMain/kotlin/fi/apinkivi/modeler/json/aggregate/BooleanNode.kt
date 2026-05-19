package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("boolean")
data class BooleanNode(
    var trueCount: Int = 0,
    var falseCount: Int = 0,
    override var nullCount: Int = 0,
) : PrimitiveNode {
    override val constant get() = trueCount > falseCount

    override val last get() = constant

    override val nodeTypeSymbol get() = "⚖️" // 🚦 ⚖

    override val view get() = "$trueCount|$falseCount"

    constructor(boolean: Boolean) : this(if (boolean) 1 else 0, if (boolean) 0 else 1)

    override fun observe(value: Boolean) = apply { if (value) trueCount++ else falseCount++ }

    override fun promote(data: Data): Node {
        val text = data.toString()
        if (text.lowercase().mightBeBoolean()) {
            logger.error { "$log Add support for string and number values: $text" }
        }
        logger.warn { "$log $trueCount/$falseCount -> $text" }
        return DynamicNode(nullCount, booleanNode = this).observe(data)
    }

    override fun symbol(value: Any?) = value?.let { if (it as Boolean) TRUE else FALSE } ?: NULL

    override fun toString() = view

    companion object {
        const val NULL = NullNode.VALUE
        const val TRUE = "✅" // ✔
        const val FALSE = "❌" // ✖

        @Suppress("SpellCheckingInspection")
        private val booleanChars = "tfyn10-ke".toCharArray()

        fun String.mightBeBoolean() = booleanChars.any { it in this }
    }
}
