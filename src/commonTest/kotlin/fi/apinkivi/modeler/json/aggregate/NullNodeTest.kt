package fi.apinkivi.modeler.json.aggregate

import fi.apinkivi.modeler.json.Data
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NullNodeTest : Logging() {
    private fun create(test: NullNode.() -> Unit) = NullNode().test()

    @Test fun observe_NonNull_null_thisAnd1() =
        create {
            assertEquals(this, observe(nullData))
            assertEquals(1, nullCount)
        }

    @Test fun observe_NonNull_3Nulls_3AndThis() =
        create {
            repeat(3) { assertEquals(this, observe(nullData)) }
            assertEquals(3, nullCount)
        }

    @Test fun observe_NonNull_array_notThis() =
        create {
            assertNotEquals(this, observe(JsonArray(emptyList())))
        }

    companion object {
        val nullData = Data(JsonNull)
    }
}
