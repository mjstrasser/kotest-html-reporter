package mjs.kotest

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Simple, recursive representation of a test report.
 *
 * Two properties are mutable in some way for speed of processing.
 */
@Serializable
internal data class TestReport(
    val name: String,
    var result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    val reports: MutableList<TestReport> = mutableListOf(),
)

internal fun TestReport.addChildReport(childReport: TestReport) {
    this.reports.add(childReport)
}

internal fun TestReport.toJson(): String =
    Json.encodeToString(this)
