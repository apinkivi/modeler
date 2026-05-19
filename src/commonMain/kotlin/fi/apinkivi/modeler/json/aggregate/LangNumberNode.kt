@file:UseContextualSerialization(forClasses = [Number::class])

package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization

/** Language supported number node.*/
@Serializable
@SerialName("number")
data class LangNumberNode(
    @Contextual
    override val numberStat: Counter<String> = linkedMapOf(), // TODO Try reversed map or array in array
    override var nullCount: Int = 0,
) : NumberNode {
    override val constant get() = numberStat.keys.single()

    override val last get() = numberStat.keys.last()

    override val nodeType get() = "Number"

    override val view get() = last

    constructor(number: Number) : this(linkedMapOf(number.toString() to 1))

    override fun observeNumber(data: Data): Node =
        try {
            val number = data.asNumber
            if (number is Long && number in MillisNode.range) {
                MillisNode(number).apply { setObservedNulls(nullCount) }
            } else {
                numberStat.inc(number.toString())
                this
            }
        } catch (e: NumberFormatException) {
            logger.warn(e) { "$log ${numberStat.keys.last()} -> big number $data" }
            BigNumberNode(
                // numberStat.entries.associateTo(LinkedHashMap()) { (key, value) -> key.toString() to value },
                numberStat,
                nullCount,
            ).observeNumber(data)
        }

    override fun promote(data: Data): Node {
        logger.warn { "${numberStat.keys.last()} -> $data" }
        return DynamicNode(nullCount, numberNode = this).observe(data)
    }

    override fun toString() = view
}
