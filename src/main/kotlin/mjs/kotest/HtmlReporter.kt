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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.KClass

/**
 * Kotest extension that creates an HTML report from a test run (project).
 *
 * @param outputDir the directory, relative to the build directory, where reports are written.
 * @param writeJsonReports write the reports for individual specs as JSON (for checking).
 */
class HtmlReporter(
    private val outputDir: String = "reports/kotest",
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
        val htmlReport = buildHtmlReport(specReports)
        writeReportFile(outputDir, "kotest-report.html", htmlReport)
    }

    private fun buildHtmlReport(specReports: List<SpecReport>): String {
        val builder = StringBuilder()
        builder.append(htmlHead)
        builder.append("\n<h1>Test Results</h1>")
        val now = ZonedDateTime.now()
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.LONG))
        builder.append("\n<p class='timestamp'>At $now</p>")

        buildToc(builder, specReports)

        specReports.forEach { buildSpecHtml(builder, it) }

        builder.append(htmlFoot)
        return builder.toString()
    }

    private fun buildToc(builder: StringBuilder, specReports: List<SpecReport>) {
        builder.append("\n<h2 id='toc'>Specs</h2>")
        specReports.forEach { buildTocEnty(builder, it) }
    }

    private val SpecReport.anchor: String
        get() = this.name

    private fun buildTocEnty(builder: StringBuilder, specReport: SpecReport) {
        val result = if (specReport.result == "Success") "✓" else "x"
        builder.append("\n<div class='test-result'>")
        builder.append("\n<div class='result-col'>$result</div>")
        builder.append("\n<div class='name-col'><a href='#${specReport.anchor}'>${specReport.name}</a></div>")
        builder.append("\n</div>")
    }

    private fun buildSpecHtml(builder: StringBuilder, specReport: SpecReport) {
        val result = if (specReport.result == "Success") "✓" else "x"
        builder.append("\n<h2 id='${specReport.anchor}'>$result&nbsp;${specReport.name} <a href='#toc'>⇧</a></h2>")

        val preamble = specReport.preamble
        if (preamble != null) {
            builder.append("\n<p class='preamble'>${preamble.replace("\n", "<br/>")}</p>")
        }
        specReport.reports.forEach { buildTestHtml(builder, it) }
    }

    private fun buildTestHtml(builder: StringBuilder, testReport: TestReport, indent: Int = 0) {
        builder.append("\n<div class='test-result'>")
        repeat(indent) {
            builder.append("\n<div class='block-col'></div>")
        }
        val result = if (testReport.result == "Success") "✓" else "x"
        builder.append("\n<div class='result-col'><span class='result'>$result</span></div>")
        val name = convertMarkdown(testReport.name)
        builder.append("\n<div class='name-col'><span class='name'>$name</span></div>")
        val duration = testReport.duration ?: ""
        builder.append("\n<div class='duration-col'><span class='duration'>$duration</span></div>")
        builder.append("\n</div>")
        testReport.reports.forEach { buildTestHtml(builder, it, indent + 1) }
    }

    private val codeRegex = Regex("`([^`]+)`")
    private fun convertMarkdown(str: String): String = codeRegex.replace(str, "<span class='code'>$1</span>")

    private val htmlHead = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Test Results</title>
            <style>
                * { font-family: sans-serif; }
                .code { font-family: monospace; background-color: #EEEEEE; }
                .timestamp { font-size: 1.2em; }
                .duration { color: gray; }
                .test-result { display: flex; }
                .block-col { flex: 0 0 1em; margin: 2px; }
                .result-col { flex: 0 0 1em; margin: 2px; }
                .name-col { flex: 1; background-color: ivory; margin: 2px; }
                .duration-col { flex: 0 0 6em; text-align: right; margin: 2px; }
            </style>
        </head>
        <body>
    """.trimIndent()
    private val htmlFoot = """
        </body>
        </html>
    """.trimIndent()
}
