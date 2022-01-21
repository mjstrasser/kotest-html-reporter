package mjs.kotest

import kotlinx.serialization.Serializable

@Serializable
data class TestReport(
    val name: String,
    val result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    val reports: List<TestReport> = listOf(),
)

fun TestReport.prependChild(child: TestReport): TestReport =
    TestReport(name, result, duration, message, listOf(child) + reports)
