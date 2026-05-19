package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("object")
class ObjectNode(
    private val properties: LinkedHashMap<String, Property> = linkedMapOf(),
    override val sizeStat: Counter<Int> = linkedMapOf(),
    override var nullCount: Int = 0,
) : SizedNode,
    MutableMap<String, Property> by properties {
    override val nodeTypeSymbol get() = "📦"

    override val view get() = "{${properties.values.joinToString()}}"

    constructor(obj: JsonObject) : this() {
        observe(obj)
    }

    override fun observe(value: JsonObject): Node {
        var index = 0
        value.forEach { (name, element) ->
            var property = properties[name]
            val data = Data(element)
            if (property == null) {
                property = Property(data.node)
                properties[name] = property
            } else {
                property.observe(data)
            }
            property.indexStat.inc(index)
            index++
        }
        observeSize(index)
        properties.forEach { (name, property) ->
            if (!value.containsKey(name)) property.missingCount++
        }
        return this
    }

    override fun promote(data: Data): Node {
        logger.warn { "$log $view -> $data" }
        return DynamicNode(nullCount, objectNode = this).observe(data)
    }

    override fun toString() = view
}
