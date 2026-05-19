package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import fi.apinkivi.modeler.json.inc
import kotlinx.serialization.Serializable

@Serializable
sealed interface SizedNode : NullableNode {
    val sizeStat: Counter<Int>

    fun observeSize(size: Int) = sizeStat.inc(size)
}
