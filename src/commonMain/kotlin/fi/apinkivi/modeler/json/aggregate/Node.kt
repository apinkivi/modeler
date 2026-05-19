package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.common.hash
import fi.apinkivi.modeler.json.Data
import io.klogging.NoCoLogging
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

/** Represents observed JSON aggregate tree node. */
@Serializable
sealed interface Node : NoCoLogging {
    /** Logging prefix. */
    val log get() = "#$hash"

    val nodeType get() = this::class.simpleName!!.removeSuffix("Node")

    val nodeTypeSymbol: String? get() = null

    val nullCount: Int get() = (this as? NullableNode)?.nullCount ?: 0

    /** Value of the node viewed in logs. */
    val view: String

    val valueString: String get() = "TODO"

    fun observe(data: Data) =
        when (data.type) {
            Data.Type.NULL -> observeNull()
            Data.Type.STRING -> observe(data.asString)
            Data.Type.BOOLEAN -> observe(data.asBoolean)
            Data.Type.NUMBER -> observeNumber(data)
            Data.Type.OBJECT -> observe(data.asObject)
            Data.Type.ARRAY -> observe(data.asArray)
        }

    fun observeNull(): Node

    /** Set the number of nulls observed by [NullNode] after promotion. */
    fun setObservedNulls(nulls: Int)

    fun observe(value: String) = promote(Data(value))

    fun observe(value: Boolean) = promote(Data(value))

    fun observeNumber(data: Data) = promote(data)

    fun observe(value: JsonObject) = promote(Data(value))

    fun observe(value: JsonArray) = promote(Data(value))

    /** Wide node to support more data types.*/
    fun promote(data: Data): Node

    fun symbol(value: Any?): String? = null
}
