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

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.scopes.RootScope
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Build a [SpecReport] with a tree of [TestReport] instances for all supplied
 * [TestResult] instances.
 */
internal object SpecReportBuilder {

    private const val FAILURE = "Failure"
    private const val CHILD_FAILURE = "ChildFailure"

    internal fun reportFromResults(className: String, results: Map<TestCase, TestResult>): SpecReport {

        val specReport = SpecReport(className)

        val reports = reportsFromResults(results)
        val casesInSourceOrder = results.keys.sortedBy { it.sourceAndLine }
        casesInSourceOrder.forEach { case ->
            val report = reports[case]!!
            val parent = case.parent
            if (parent != null) {
                val parentReport = reports[parent]!!
                parentReport.addChildReport(report)
            } else {
                specReport.description = rootScopeDescription(case.spec)
                specReport.addChildReport(report)
            }
        }

        setChildFailuresOnContainers(specReport)

        return specReport
    }

    private fun rootScopeDescription(spec: Spec): String? = when(spec) {
        is RootScope -> spec.description
        else -> null
    }

    /** Map [TestResult]s to [TestReport]s, keyed by the same [TestCase]s. */
    private fun reportsFromResults(results: Map<TestCase, TestResult>): Map<TestCase, TestReport> =
        results.mapValues { (case, result) ->
            TestReport(
                name = case.reportingName,
                result = result.name,
                duration = durationInMsIfPositive(result),
                message = result.errorOrNull?.stackTraceToString(),
            )
        }

    /** Format a positive [Duration] to string in milliseconds, else null.  */
    private fun durationInMsIfPositive(result: TestResult): String? =
        if (result.duration > Duration.ZERO)
            result.duration.toString(DurationUnit.MILLISECONDS, 2)
        else null

    /**
     * Set [TestReport.result] value to "ChildFailure" if any of its descendant reports
     * has "Failure".
     */
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
