/*
   Copyright 2022-2025 Michael Strasser.

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
import kotlinx.html.details
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
import kotlinx.html.summary
import kotlinx.html.title
import kotlinx.html.unsafe
import mjs.kotest.ReadResource.readResourceText
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TICK = "✓"
private const val CROSS = "x"
private const val DASH = "–"
private const val TO_TOP = "⇧"
private const val GIT_COMMIT = "GIT_COMMIT"
private const val GIT_MESSAGE = "GIT_MESSAGE"
private const val TIMEZONE = "TIMEZONE"

/**
 * Build a test report in HTML from a list of [SpecReport]s.
 */
internal class HtmlReportBuilder(
    private val specReports: List<SpecReport>,
) {

    private val css = readResourceText("mjs/kotest/html-reporter.css") ?: DefaultCss
    private val source: String = System.getenv(GIT_COMMIT) ?: ""
    private val gitMessage: String = System.getenv(GIT_MESSAGE) ?: ""

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
        }
    }

    private fun HTML.reportBody() {
        body {
            h1 { +"Test Results" }
            p("timestamp") {
                +"Time: ${now()}"
                if (source.isNotEmpty()) {
                    br
                    +"Commit: $source"
                }
                if (gitMessage.isNotEmpty()) {
                    br
                    fromMarkdown("Message: ${gitMessage.firstLine}")
                }
            }
            toc()
            results()
            tagline()
        }
    }

    private fun BODY.toc() {
        div("toc") {
            h2 {
                id = "toc"
                +"Specs"
            }
            div("toc-entries shadow-box") {
                specReports.forEach {
                    tocEntry(it)
                }
            }
        }
    }

    private fun DIV.tocEntry(specReport: SpecReport) {
        div("toc-line") {
            div("result-col") { span("result-${specReport.result}") { +specReport.symbol } }
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
            span("result-${specReport.result}") { +specReport.symbol }
            nbsp
            +specReport.name
            nbsp
            a("#toc") { +TO_TOP }
        }
        specReport.description?.let { desc ->
            div("line") {
                div("block-col")
                p("shadow-box") { fromMarkdown(desc) }
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
        if (indent == 0 && testReport.reports.isNotEmpty()) {
            div("pre-test")
        }
        div("line") {
            repeat(indent) { div("block-col") }
            div("result-col") { span("result-${testReport.result}") { +testReport.symbol } }
            val nameClasses = "name-col ${testReport.result ?: ""}"
            div(nameClasses) {
                testReport.message?.let { msg ->
                    details {
                        summary("name") {
                            fromMarkdown(testReport.name)
                            pre("error-message") { +(msg.firstLine) }
                        }
                        pre("error-message") { +(msg.remainingLines) }
                    }
                } ?: span("name") { fromMarkdown(testReport.name) }
            }
            div("duration-col") { span("duration") { +(testReport.duration ?: "") } }
        }
        testReport.reports.forEach { test(it, indent + 1) }
    }

    private val SpecReport.anchor: String
        get() = this.name

    private val Report.symbol: String
        get() = when (this.result) {
            "Ignored" -> DASH
            "Success" -> TICK
            else -> CROSS
        }

    private val String.firstLine: String
        get() = this.lines().first()

    private val String.remainingLines: String
        get() = this.lines().drop(1).joinToString("\n")

    private fun now(): String {
        val zone = System.getenv(TIMEZONE)
        val zoneId = try {
            ZoneId.of(zone)
        } catch (e: Exception) {
            ZoneId.systemDefault()
        }
        return ZonedDateTime.ofInstant(Instant.now(), zoneId)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    private fun BODY.tagline() {
        div("shadow-box tagline") {
            +"Report generated by "
            a(
                href = "https://github.com/mjstrasser/kotest-html-reporter#kotest-html-reporter",
                target = "_blank",
                classes = "tagline",
            ) {
                +"Kotest HTML Reporter"
            }
        }
    }
}
