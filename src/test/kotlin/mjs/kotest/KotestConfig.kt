package mjs.kotest

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

class KotestConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(
        HtmlReporter(writeJsonReports = true),
    )
}
