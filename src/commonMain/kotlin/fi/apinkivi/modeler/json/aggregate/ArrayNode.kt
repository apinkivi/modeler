package fi.apinkivi.modeler.json.aggregate

import kotlinx.serialization.Serializable

@Serializable
sealed interface ArrayNode : SizedNode {
    override val nodeTypeSymbol get() = "🎞️" // ▤ ☰
}
