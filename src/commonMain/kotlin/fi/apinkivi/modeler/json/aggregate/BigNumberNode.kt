package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Big numbers that must be stored as strings.
 * TODO How to relates JS BigInt?
 */
@Serializable
@SerialName("big number")
data class BigNumberNode(
    @Contextual
    override val numberStat: Counter<String> = linkedMapOf(),
    override var nullCount: Int = 0,
) : NumberNode {
    override val constant get() = numberStat.keys.single()

    override val last get() = numberStat.keys.last()

    override val view get() = last

    constructor(number: String) : this(linkedMapOf(number to 1))

    override fun observeNumber(data: Data) = apply { numberStat.inc(data.asString) }

    override fun promote(data: Data): Node {
        logger.warn { "${numberStat.keys.last()} -> $data" }
        return DynamicNode(nullCount, numberNode = this).observe(data)
    }

    override fun toString() = view
}
