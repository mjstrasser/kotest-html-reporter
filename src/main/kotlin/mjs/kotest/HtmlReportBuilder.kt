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
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe
import mjs.kotest.ReadResource.readResourceText
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val TICK = "✓"
private const val CROSS = "x"
private const val DASH = "–"
private const val TO_TOP = "⇧"
private const val GIT_COMMIT = "GIT_COMMIT"

/**
 * Build a test report in HTML from a list of [SpecReport]s.
 */
internal class HtmlReportBuilder(
    private val specReports: List<SpecReport>,
) {

    private val css = readResourceText("html-reporter.css") ?: ""
    private val javascript = readResourceText("html-reporter.js") ?: ""

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
            style { unsafe { raw(css) } }
            script { unsafe { raw(javascript) } }
        }
    }

    private fun HTML.reportBody() {
        body {
            h1 { +"Test Results" }
            p("timestamp") {
                +"Time: $now"
                if (source != null) {
                    br
                    +"Commit: $source"
                }
            }
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
            div("toc-entries") {
                specReports.forEach {
                    tocEntry(it)
                }
            }
        }
    }

    private fun DIV.tocEntry(specReport: SpecReport) {
        div("toc-line") {
            div("result-col") { span("result") { +result(specReport) } }
            div("toc-col") {
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
            val msgId = "msg-${testReport.hashCode()}"
            val nameClasses = "name-col ${testReport.result ?: ""}"
            div(nameClasses) {
                if (testReport.hasMessage)
                    onClick = "toggleItem('$msgId')"
                span("name") { fromMarkdown(testReport.name) }
                testReport.message?.let { msg ->
                    div("error-message") {
                        id = msgId
                        style = "display:none"
                        pre { +msg }
                    }
                }
            }
            div("duration-col") { span("duration") { +(testReport.duration ?: "") } }
        }
        testReport.reports.forEach { test(it, indent + 1) }
    }

    private val now = ZonedDateTime.now()
        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.LONG))

    private val source: String? = System.getenv(GIT_COMMIT)

    private val SpecReport.anchor: String
        get() = this.name

    private fun result(report: Report) = when (report.result) {
        "Ignored" -> DASH
        "Success" -> TICK
        else -> CROSS
    }
}
