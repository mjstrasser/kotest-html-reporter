package mjs.kotest

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A report that can have child reports.
 *
 * Reports use mutable fields for speed and convenience.
 */
internal interface Report {
    val reports: MutableList<TestReport>
    fun addChildReport(childReport: TestReport) {
        reports.add(childReport)
    }
}

/** Report for a test case. */
@Serializable
internal data class TestReport(
    val name: String,
    var result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report

/** Report for a spec. */
@Serializable
internal data class SpecReport(
    val name: String,
    var preamble: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report {
    internal fun toJson(): String =
        Json.encodeToString(this)
}