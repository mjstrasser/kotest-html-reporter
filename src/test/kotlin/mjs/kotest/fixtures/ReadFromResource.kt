package mjs.kotest.fixtures

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mjs.kotest.TestReport

internal fun readTestReport(resourcePath: String): TestReport? {
    val text = readResourceText(resourcePath)
    return text?.let {
        testReportFromString(it)
    }
}

internal fun readResourceText(resourcePath: String): String? =
    ClassLoader.getSystemClassLoader()
        .getResourceAsStream(resourcePath)
        ?.bufferedReader(Charsets.UTF_8)
        ?.let {
            it.readText()
        }

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

internal fun testReportFromString(reportString: String): TestReport? =
    try {
        json.decodeFromString(reportString)
    } catch (ex: SerializationException) {
        null
    }
