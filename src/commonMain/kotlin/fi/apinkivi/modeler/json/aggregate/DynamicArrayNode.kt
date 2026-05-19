package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import java.util.function.IntFunction

@Serializable
@SerialName("dynamic array")
class DynamicArrayNode(
    private val elements: MutableList<Node> = mutableListOf(),
    override val sizeStat: Counter<Int> = linkedMapOf(),
    override var nullCount: Int = 0,
) : ArrayNode,
    List<Node> by elements {
    override val view get() = "[${joinToString()}]"

    constructor(array: JsonArray) : this() {
        observe(array)
    }

    override fun observe(value: JsonArray): Node {
        value.forEachIndexed { index, element ->
            val container = elements.getOrNull(index)
            val data = Data(element)
            if (container == null) {
                elements.add(data.node)
            } else {
                elements[index] = container.observe(data)
            }
        }
        observeSize(value.size)
        return this
    }

    override fun promote(data: Data): Node {
        logger.warn { "$log $view -> $data" }
        return DynamicNode(nullCount, arrayNode = this).observe(data)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Use toArray(IntFunction<Array<T>>) instead")
    override fun <T> toArray(generator: IntFunction<Array<T>>): Array<T> = super.toArray(generator)

    override fun toString() = view
}
