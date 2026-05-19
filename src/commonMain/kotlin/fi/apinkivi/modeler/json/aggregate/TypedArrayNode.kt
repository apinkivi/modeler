package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
@SerialName("array")
class TypedArrayNode(
    /** Value node of all items in the array. Null means always empty. */
    var valueNode: Node? = null,
    val indexStat: Counter<Int> = linkedMapOf(),
    override val sizeStat: Counter<Int> = linkedMapOf(),
    override var nullCount: Int = 0,
) : ArrayNode {
    override val view get() = "[${valueNode?.view ?: ""}]"

    constructor(array: JsonArray) : this() {
        observe(array)
    }

    override fun observe(value: JsonArray): Node {
        value.forEachIndexed { index, element ->
            indexStat.inc(index)
            val data = Data(element)
            valueNode = valueNode?.observe(data) ?: Data(element).node
        }
        observeSize(value.size)
        return this
    }

    override fun promote(data: Data): Node {
        // TODO dynamic array?
        logger.warn { "$log $view -> $data" }
        return DynamicNode(nullCount, arrayNode = this).observe(data)
    }

    override fun toString() = view
}
