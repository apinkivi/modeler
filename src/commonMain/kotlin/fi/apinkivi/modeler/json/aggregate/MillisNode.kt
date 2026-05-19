package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/** [UNIX time](https://currentmillis.com/) in milliseconds. */
@Serializable
@SerialName("millis")
class MillisNode(
    var first: Instant? = null,
    var count: Int = 0,
    override var last: Instant? = null,
    override var nullCount: Int = 0,
) : NumberNode {
    override val constant get() = last

    override val nodeTypeSymbol get() = "⏱️"

    override val numberStat get() = linkedMapOf(last.toString() to 1)

    override val view get() = last.toString()

    constructor(long: Long) : this(Instant.fromEpochMilliseconds(long), 1, Instant.fromEpochMilliseconds(long))

    override fun observeNumber(data: Data) =
        try {
            val number = data.asLong
            if (number in range || number == 0L) {
                last = Instant.fromEpochMilliseconds(number)
                if (first == null) first = last
                count++
                this
            } else {
                promote(data)
            }
        } catch (e: NumberFormatException) {
            logger.warn(e) { "$log $last -> big number $data" }
            BigNumberNode().observeNumber(data).apply { setObservedNulls(nullCount) }
        }

    override fun promote(data: Data): Node {
        logger.error { "$log $last -> $data" }
        return LangNumberNode(data.asNumber).apply { setObservedNulls(nullCount) }
    }

    companion object {
        /** Year 2017 */
        const val MIN = 1_500_000_000_000

        /** Year 2049 */
        const val MAX = 2_500_000_000_000
        val range = MIN..MAX
    }
}
