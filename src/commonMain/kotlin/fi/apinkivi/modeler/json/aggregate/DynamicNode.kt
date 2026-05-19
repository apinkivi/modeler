package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("dynamic")
class DynamicNode(
    override var nullCount: Int = 0,
    val stringNode: StringNode = StringNode(),
    val booleanNode: BooleanNode = BooleanNode(),
    val numberNode: NumberNode = LangNumberNode(),
    val objectNode: ObjectNode = ObjectNode(),
    val arrayNode: ArrayNode = TypedArrayNode(),
) : NullableNode {
    init {
        stringNode.nullCount = 0
        booleanNode.nullCount = 0
        numberNode.nullCount = 0
        objectNode.nullCount = 0
        arrayNode.nullCount = 0
    }

    override val view get() = "dynamic"

    override fun observe(value: String) = stringNode.observe(value)

    override fun observe(value: Boolean) = booleanNode.observe(value)

    override fun observeNumber(data: Data) = numberNode.observeNumber(data)

    override fun observe(value: JsonObject) = objectNode.observe(value)

    override fun observe(value: JsonArray) = arrayNode.observe(value)

    override fun promote(data: Data) = error("Can't promote dynamic node by: $data")

    override fun toString() = view
}
