package mjs.kotest

import kotlinx.html.BODY
import kotlinx.html.DIV
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import kotlinx.html.title
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val TICK = "✓"
private const val CROSS = "x"
private const val TO_TOP = "⇧"

/**
 * Build a test report in HTML from a list of [SpecReport]s.
 */
internal class HtmlReportBuilder(
    private val specReports: List<SpecReport>,
) {

    private val now: String
        get() = ZonedDateTime.now()
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.LONG))

    private val builder = StringBuilder()

    internal fun build(): String {
        builder.append(htmlHead)

        builder.append("\n<h1>Test Results</h1>")
        builder.append("\n<p class='timestamp'>At $now</p>")

        buildToc()
        buildSpecReports()

        builder.append(htmlFoot)

        return builder.toString()
//        return newBuild()
    }

    internal fun newBuild(): String = buildString {
        appendLine("<!DOCTYPE html>")
        appendHTML().html {
            reportHead()
            reportBody()
        }
    }

    private fun HTML.reportHead() {
        head {
            title { +"Test Results" }
            meta(charset = "UTF-8")
            style {
                +"""
                            * { font-family: sans-serif; }
                            .code { font-family: monospace; background-color: #EEEEEE; }
                            .timestamp { font-size: 1.2em; }
                            .duration { color: gray; }
                            .line { display: flex; }
                            .block-col { flex: 0 0 1em; margin: 2px; }
                            .result-col { flex: 0 0 1em; margin: 2px; }
                            .name-col { flex: 1; background-color: ivory; margin: 2px; }
                            .duration-col { flex: 0 0 6em; text-align: right; margin: 2px; }
                            .description { background-color: ivory; margin: 2px; }
                        """.trimIndent()
            }
        }
    }

    private fun HTML.reportBody() {
        body {
            h1 { +"Test Results" }
            p("timestamp") { +"At $now" }
            reportToc()
            reportResults()
        }
    }

    private fun BODY.reportToc() {
        div("toc") {
            h2 {
                id = "toc"
                +"Specs"
            }
            specReports.forEach {
                tocEntry(it)
            }
        }
    }

    private fun DIV.tocEntry(specReport: SpecReport) {
        div("line") {
            div("result-col") { +result(specReport) }
            div("name-col") {
                a("#${specReport.anchor}") { +specReport.name }
            }
        }
    }

    private fun BODY.reportResults() {

    }

    private fun DIV.reportSpecs(specReport: SpecReport) {
        div("specs") {
            specReport.reports.forEach {
                reportTest(it)
            }
        }
    }

    private fun DIV.reportTest(testReport: TestReport) {

    }

    private fun buildToc() {
        builder.append("\n<div class='toc'>")
        builder.append("\n<h2 id='toc'>Specs</h2>")
        specReports.forEach { buildTocEntry(it) }
        builder.append("\n</div>")
    }

    private val SpecReport.anchor: String
        get() = this.name

    private fun result(specReport: SpecReport) = if (specReport.result == "Success") TICK else CROSS

    private fun buildTocEntry(specReport: SpecReport) {
        line {
            builder.append("\n<div class='result-col'>${result(specReport)}</div>")
            builder.append("\n<div class='name-col'><a href='#${specReport.anchor}'>${specReport.name}</a></div>")
        }
    }

    private fun div(cssClass: String?, body: () -> Unit) {
        val css = if (cssClass != null) " class='$cssClass'" else ""
        builder.append("\n<div$css>")
        body()
        builder.append("\n</div>")
    }

    private fun line(body: () -> Unit) {
        div("line") { body() }
    }

    private fun buildSpecReports() {
        builder.append("\n<div class='specs'>")
        specReports.forEach { buildSpecHtml(it) }
        builder.append("\n</div>")
    }

    private fun buildSpecHtml(specReport: SpecReport) {
        builder.append(
            "\n<h2 id='${specReport.anchor}'>${result(specReport)}&nbsp;${specReport.name}&nbsp;<a href='#toc'>$TO_TOP</a></h2>"
        )
        val description = specReport.description
        if (description != null) {
            line {
                builder.append("\n<div class='block-col'></div>")
                builder.append("\n<p class='description'>${description.convertMarkdown()}</p>")
            }
        }
        specReport.reports.forEach { buildTestHtml(it) }
    }

    private fun buildTestHtml(testReport: TestReport, indent: Int = 0) {

        line {
            repeat(indent) { builder.append("\n<div class='block-col'></div>") }

            val result = if (testReport.result == "Success") TICK else CROSS
            builder.append("\n<div class='result-col'><span class='result'>$result</span></div>")
            builder.append("\n<div class='name-col'>")
            builder.append("<span class='name'>${testReport.name.convertMarkdown()}</span>")

            if (testReport.result == "Failure")
                builder.append("<br>\n<pre>${testReport.message}</pre>")

            builder.append("</div>")
            val duration = testReport.duration ?: ""
            builder.append("\n<div class='duration-col'><span class='duration'>$duration</span></div>")
        }

        testReport.reports.forEach { buildTestHtml(it, indent + 1) }
    }

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
                .line { display: flex; }
                .block-col { flex: 0 0 1em; margin: 2px; }
                .result-col { flex: 0 0 1em; margin: 2px; }
                .name-col { flex: 1; background-color: ivory; margin: 2px; }
                .duration-col { flex: 0 0 6em; text-align: right; margin: 2px; }
                .description { background-color: ivory; }
            </style>
        </head>
        <body>
    """.trimIndent()
    private val htmlFoot = """
        </body>
        </html>
    """.trimIndent()
}
