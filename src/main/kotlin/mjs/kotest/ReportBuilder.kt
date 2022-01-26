package mjs.kotest

import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlin.time.Duration
import kotlin.time.DurationUnit

object ReportBuilder {

    private const val FAILURE = "Failure"
    private const val CHILD_FAILURE = "ChildFailure"

    internal fun reportFromResults(className: String, results: Map<TestCase, TestResult>): SpecReport {

        val reports = results.mapValues { (case, result) ->
            TestReport(
                name = case.reportingName,
                result = result.name,
                duration = durationIfPositive(result),
                message = result.errorOrNull?.stackTraceToString(),
            )
        }
        val orderedCases = results.keys.sortedBy { it.sourceAndLine }
        val specReport = SpecReport(className)
        orderedCases.forEach { case ->
            val report = reports[case]!!
            val parent = case.parent
            if (parent != null) {
                val parentReport = reports[parent]!!
                parentReport.addChildReport(report)
            } else {
                specReport.preamble = case.spec.preamble
                specReport.addChildReport(report)
            }
        }

        setChildFailuresOnContainers(specReport)

        return specReport
    }

    private fun durationIfPositive(result: TestResult): String? =
        if (result.duration > Duration.ZERO)
            result.duration.toString(DurationUnit.MILLISECONDS, 3)
        else null

    private fun setChildFailuresOnContainers(report: SpecReport) {
        fun setChildFailures(report: TestReport) {
            report.reports.forEach {
                setChildFailures(it)
                if (it.result == FAILURE || it.result == CHILD_FAILURE)
                    report.result = CHILD_FAILURE
            }
        }
        report.reports.forEach { setChildFailures(it) }
    }
}
