package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("string")
data class StringNode(
    var valueStat: Counter<String> = linkedMapOf(),
    override var nullCount: Int = 0,
) : PrimitiveNode {
    override val constant get() = valueStat.keys.single()

    override val last get() = valueStat.keys.last()

    override val nodeTypeSymbol get() = "📜" // 💬 ✎

    override val valueString: String get() =
        try {
            valueStat.keys.single()
        } catch (e: IllegalArgumentException) {
            e.message ?: e.toString()
        }

    override val view get() = "\"$last\""

    constructor(string: String) : this(linkedMapOf(string to 1))

    override fun observe(value: String) = apply { valueStat.inc(value) }

    override fun promote(data: Data): Node {
        logger.warn { """$log "${valueStat.keys.last()}" -> $data""" }
        return DynamicNode(nullCount, this).observe(data)
    }

    override fun toString() = view
}
