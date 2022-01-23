package mjs.kotest

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface Report {
    fun addChildReport(childReport: TestReport)
}

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
) : Report {

    override fun addChildReport(childReport: TestReport) {
        reports.add(childReport)
    }
}

@Serializable
internal data class SpecReport(
    val name: String,
    var preamble: String? = null,
    val reports: MutableList<TestReport> = mutableListOf(),
) : Report {
    override fun addChildReport(childReport: TestReport) {
        reports.add(childReport)
    }

    internal fun toJson(): String =
        Json.encodeToString(this)
}