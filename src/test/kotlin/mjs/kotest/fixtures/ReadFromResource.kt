/*

   Copyright 2022 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

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
