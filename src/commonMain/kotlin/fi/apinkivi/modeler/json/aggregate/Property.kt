package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Property(
    var valueNode: Node,
    val indexStat: Counter<Int> = linkedMapOf(),
    var missingCount: Int = 0,
) : Node {
    override val view get() = error("Ask from object")

    override fun observe(data: Data): Node {
        valueNode = valueNode.observe(data)
        return this
    }

    /** Observing null shouldn't cause promotion. */
    override fun observeNull() = valueNode.observeNull()

    override fun setObservedNulls(nulls: Int) = valueNode.setObservedNulls(nulls)

    override fun promote(data: Data) = error("Can't promote property by: $data")
}
