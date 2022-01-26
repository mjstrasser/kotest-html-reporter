package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import mjs.kotest.ReportBuilder.reportFromResults
import mjs.kotest.ReportWriter.writeReportFile
import kotlin.reflect.KClass

class HtmlReporter(
    private val outputDir: String = "reports/kotest",
    private val writeJsonReports: Boolean = true,
) : FinalizeSpecListener, AfterProjectListener, Klogging {

    private val specReports: MutableList<SpecReport> = mutableListOf()

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val className = kclass.qualifiedName!!
        val specReport = reportFromResults(className, results)
        specReports.add(specReport)
        if (writeJsonReports)
            writeReportFile(outputDir, "$className.json", specReport.toJson())
    }

    override suspend fun afterProject() {
        if (specReports.isEmpty()) return
        val htmlReport = buildHtmlReport(specReports)
        writeReportFile(outputDir, "kotest-report.html", htmlReport)
    }

    private fun buildHtmlReport(specReports: List<SpecReport>): String {
        val builder = StringBuilder()
        builder.append(htmlHead)

        specReports.forEach { buildSpecHtml(builder, it) }

        builder.append(htmlFoot)
        return builder.toString()
    }

    private fun buildSpecHtml(builder: StringBuilder, specReport: SpecReport) {
        builder.append("\n<h2>${specReport.name}</h2>")

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
        builder.append("\n<div class='name-col'><span class='name'>${testReport.name}</span></div>")
        val duration = testReport.duration ?: ""
        builder.append("\n<div class='duration-col'><span class='duration'>$duration</span></div>")
        builder.append("\n</div>")
        testReport.reports.forEach { buildTestHtml(builder, it, indent + 1) }
    }

    private val htmlHead = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Test Results</title>
            <style>
                * { font-family: sans-serif; }
                .test-result { display: flex; }
                .block-col { flex: 0 0 2em; }
                .result-col { flex: 0 0 2em; }
                .name-col { flex: 1; background-color: ivory; }
                .duration-col { flex: 0 0 8em; text-align: right; font-colour: gray; }
            </style>
        </head>
        <body>
        <h1>Test Report</h1>
    """.trimIndent()
    private val htmlFoot = """
        </body>
        </html>
    """.trimIndent()
}