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
