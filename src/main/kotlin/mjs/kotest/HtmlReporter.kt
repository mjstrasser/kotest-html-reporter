package mjs.kotest

import io.kotest.core.listeners.AfterProjectListener

class HtmlReporter(
    private val outputDir: String = "reports/kotest",
) : AfterProjectListener {
    override suspend fun afterProject() {
        
    }
}