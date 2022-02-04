package mjs.kotest

import kotlinx.html.BODY
import kotlinx.html.DIV
import kotlinx.html.HTML
import kotlinx.html.HTMLTag
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe
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

    internal fun build(): String = buildString {
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
                            .description { background-color: ivory; padding: 2px; }
                        """.trimIndent()
            }
        }
    }

    private fun HTML.reportBody() {
        body {
            h1 { +"Test Results" }
            p("timestamp") { +"At $now" }
            toc()
            results()
        }
    }

    private fun BODY.toc() {
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
            div("result-col") { span("result") { +result(specReport) } }
            div("name-col") {
                a("#${specReport.anchor}") { +specReport.name }
            }
        }
    }

    private fun BODY.results() {
        div("specs") {
            specReports.forEach {
                spec(it)
            }
        }
    }

    private fun DIV.spec(specReport: SpecReport) {
        h2 {
            id = specReport.anchor
            +result(specReport)
            nbsp
            +specReport.name
            nbsp
            a("#toc") { +TO_TOP }
        }
        specReport.description?.let { desc ->
            div("line") {
                div("block-col")
                p("description") { fromMarkdown(desc) }
            }
        }
        specReport.reports.forEach {
            test(it)
        }
    }

    private val HTMLTag.nbsp get() = unsafe { raw("&nbsp;") }

    private fun HTMLTag.fromMarkdown(markdown: String) {
        unsafe { raw(markdown.convertMarkdown()) }
    }

    private fun DIV.test(testReport: TestReport, indent: Int = 0) {
        div("line") {
            repeat(indent) { div("block-col") }
            div("result-col") { span("result") { +result(testReport) } }
            div("name-col") {
                span("name") { fromMarkdown(testReport.name) }
                if (testReport.result == "Failure") testReport.message?.let { msg ->
                    br
                    pre { +msg }
                }
            }
            div("duration-col") { span("duration") { +(testReport.duration ?: "") } }
        }
        testReport.reports.forEach { test(it, indent + 1) }
    }

    private val now: String
        get() = ZonedDateTime.now()
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.LONG))

    private val SpecReport.anchor: String
        get() = this.name

    private fun result(report: Report) = if (report.result == "Success") TICK else CROSS
}
