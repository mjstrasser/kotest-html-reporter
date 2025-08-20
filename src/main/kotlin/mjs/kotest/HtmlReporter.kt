/*
   Copyright 2022-2025 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package mjs.kotest

import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import mjs.kotest.BuildReportWriter.writeReportFile
import mjs.kotest.SpecReportBuilder.reportFromResults

/**
 * Kotest extension that creates an HTML report from a test run (project).
 *
 * @param outputDir the directory, relative to the build directory, where reports are written.
 * @param reportFilename name of the HTML report file
 */
public class HtmlReporter(
    private val outputDir: String = "reports/kotest",
    private val reportFilename: String = "kotest-report.html",
) : BeforeSpecListener,
    AfterTestListener,
    AfterProjectListener {
    /**
     * Map of maps of test results by case by spec. They are linked hash maps so insertion
     * order is preserved.
     */
    private val testResults: LinkedHashMap<String, LinkedHashMap<TestCase, TestResult>> = linkedMapOf()

    override suspend fun beforeSpec(spec: Spec) {
        spec::class.qualifiedName?.let { testResults.getOrPut(it) { linkedMapOf() } }
    }

    override suspend fun afterAny(
        testCase: TestCase,
        result: TestResult,
    ) {
        testCase.spec::class.qualifiedName?.let { specClass ->
            testResults[specClass]?.let { it[testCase] = result }
        }
    }

    /** After all specs have been run, write the [SpecReport]s into an HTML report. */
    override suspend fun afterProject() {
        val reports =
            testResults.map { (className, results) ->
                reportFromResults(className, results)
            }
        if (reports.isEmpty()) return
        val htmlReport = HtmlReportBuilder(reports).build()
        writeReportFile(outputDir, reportFilename, htmlReport)
    }
}
