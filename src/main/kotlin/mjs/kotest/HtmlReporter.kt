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

package mjs.kotest

import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import mjs.kotest.BuildReportWriter.writeReportFile
import mjs.kotest.SpecReportBuilder.reportFromResults
import kotlin.reflect.KClass

/**
 * Kotest extension that creates an HTML report from a test run (project).
 *
 * @param outputDir the directory, relative to the build directory, where reports are written.
 * @param reportFilename name of the HTML report file
 * @param writeJsonReports write the reports for individual specs as JSON (for checking).
 */
class HtmlReporter(
    private val outputDir: String = "reports/kotest",
    private val reportFilename: String = "kotest-report.html",
    private val writeJsonReports: Boolean = false,
) : FinalizeSpecListener, AfterProjectListener {

    private val specReports: MutableList<SpecReport> = mutableListOf()

    /** After each spec, write a [SpecReport] for it. */
    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val className = kclass.qualifiedName!!
        val specReport = reportFromResults(className, results)
        specReports.add(specReport)
        if (writeJsonReports)
            writeReportFile(outputDir, "$className.json", specReport.toJson())
    }

    /** After all specs have been run, write the [SpecReport]s into an HTML report. */
    override suspend fun afterProject() {
        if (specReports.isEmpty()) return
        val htmlReport = HtmlReportBuilder(specReports).build()
        writeReportFile(outputDir, reportFilename, htmlReport)
    }
}
