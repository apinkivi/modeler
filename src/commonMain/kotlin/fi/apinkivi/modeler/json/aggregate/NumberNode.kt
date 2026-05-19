package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Counter
import kotlinx.serialization.Serializable

@Serializable
sealed interface NumberNode : PrimitiveNode {
    val numberStat: Counter<*>

    override val nodeTypeSymbol get() = "🔢" // #🔢

    override val valueString: String get() = numberStat.keys.single().toString()
}
