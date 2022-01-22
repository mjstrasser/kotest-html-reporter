package mjs.kotest

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class TestReport(
    val name: String,
    val result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    val reports: MutableList<TestReport> = mutableListOf(),
)

fun TestReport.toJson(): String =
    Json.encodeToString(this)
