package fi.apinkivi.modeler.json

import fi.apinkivi.modeler.json.aggregate.BigNumberNode
import fi.apinkivi.modeler.json.aggregate.BooleanNode
import fi.apinkivi.modeler.json.aggregate.LangNumberNode
import fi.apinkivi.modeler.json.aggregate.MillisNode
import fi.apinkivi.modeler.json.aggregate.Node
import fi.apinkivi.modeler.json.aggregate.NullNode
import fi.apinkivi.modeler.json.aggregate.ObjectNode
import fi.apinkivi.modeler.json.aggregate.StringNode
import fi.apinkivi.modeler.json.aggregate.TypedArrayNode
import io.klogging.NoCoLogging
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@JvmInline
value class Data(
    val element: JsonElement,
) : NoCoLogging {
    enum class Type {
        NULL,
        STRING,
        BOOLEAN,
        NUMBER,
        OBJECT,
        ARRAY,
    }

    val type get() =
        when (element) {
            is JsonNull -> {
                Type.NULL
            }

            is JsonPrimitive -> {
                if (element.isString) {
                    Type.STRING
                } else if (element.booleanOrNull != null) {
                    Type.BOOLEAN
                } else {
                    Type.NUMBER
                }
            }

            is JsonObject -> {
                Type.OBJECT
            }

            else -> {
                Type.ARRAY
            }
        }

    /** Creates an aggregate [Node] by [Data.Type] and observes the [Data]. */
    val node get() =
        when (type) {
            Type.NULL -> NullNode(1)
            Type.STRING -> StringNode(asString)
            Type.BOOLEAN -> BooleanNode(asBoolean)
            Type.NUMBER -> asNumberNode
            Type.OBJECT -> ObjectNode(asObject)
            Type.ARRAY -> TypedArrayNode(asArray)
        }

    val objectNode get() = node as ObjectNode

    private val primitive get() = element.jsonPrimitive

    val asString get() = primitive.content

    val asBoolean get() = primitive.boolean

    val JsonPrimitive.number get(): Number = content.let { if ('.' in it || 'e' in it || 'E' in it) double else long }

    val asNumber get() = primitive.number

    val asNumberNode get() =
        try {
            asNumber.let { if (it is Long && it in MillisNode.range) MillisNode(it) else LangNumberNode(it) }
        } catch (e: NumberFormatException) {
            asString.let {
                if (logged.add(it)) logger.warn { "Creating big number and silencing it: ${e.message}" }
                BigNumberNode(it)
            }
        }

    val asLong get() = primitive.long

    val asObject get() = element.jsonObject

    val asArray get() = element.jsonArray

    constructor(value: String) : this(JsonPrimitive(value))

    constructor(value: Boolean) : this(JsonPrimitive(value))

    constructor(value: Number) : this(JsonPrimitive(value))

    override fun toString() = element.toString()

    companion object {
        /** Reported warnings that can be ignored. */
        val logged = mutableSetOf<String>()
    }
}
