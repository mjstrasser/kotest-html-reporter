package mjs

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.junitxml.JunitXmlReporter
import mjs.kotest.JsonReporter

class KotestConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(
        JsonReporter(),
    )
}
