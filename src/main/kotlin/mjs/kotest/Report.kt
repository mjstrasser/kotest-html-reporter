package mjs.kotest

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface Report {
    val reports: MutableList<TestReport>
    fun addChildReport(childReport: TestReport) {
        reports.add(childReport)
    }
}

@Serializable
internal data class TestReport(
    val name: String,
    var result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report

@Serializable
internal data class SpecReport(
    val name: String,
    var preamble: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report {
    internal fun toJson(): String =
        Json.encodeToString(this)
}