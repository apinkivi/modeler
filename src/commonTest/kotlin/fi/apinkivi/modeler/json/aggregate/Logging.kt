package fi.apinkivi.modeler.json.aggregate

import io.klogging.config.ANSI_INFO
import io.klogging.config.loggingConfiguration

open class Logging {
    val configured: Boolean

    init {
        if (configure) {
            configure = true
            configured = true
            loggingConfiguration { ANSI_INFO() }
        } else {
            configured = false
        }
    }

    companion object {
        private var configure = false
    }
}
