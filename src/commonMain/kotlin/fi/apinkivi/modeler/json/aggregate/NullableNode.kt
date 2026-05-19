package fi.apinkivi.modeler.json.aggregate

import kotlinx.serialization.Serializable

@Serializable
sealed interface NullableNode : Node {
    override var nullCount: Int

    override fun observeNull() = apply { nullCount++ }

    override fun setObservedNulls(nulls: Int) {
        this.nullCount = nulls
    }
}
