package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.isRootTest
import kotlinx.coroutines.delay
import kotlin.reflect.KClass
import kotlin.time.Duration

const val FORMAT = "%s:%04d"
internal fun TestCase.sorter(): String = source.let {
    when (it) {
        is SourceRef.FileSource -> FORMAT.format(it.fileName, it.lineNumber)
        is SourceRef.ClassSource -> FORMAT.format(it.fqn, it.lineNumber)
        else -> "zzzzzz:0000"
    }
}

class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val report = reportFromResults(kclass.qualifiedName!!, results)
//        val jsonFile = File(File(outputDir), "test-report.json")
//        jsonFile.appendText("{}")
        delay(1000)
    }

    private fun durationIfPositive(result: TestResult): String? =
        if (result.duration > Duration.ZERO) result.duration.toString()
        else null

    internal fun reportFromResults(className: String, results: Map<TestCase, TestResult>): TestReport {

        val reports = results.mapValues { (case, result) ->
            TestReport(
                name = case.name.testName,
                result = result.name,
                duration = durationIfPositive(result),
                message = result.errorOrNull?.stackTraceToString(),
            )
        }
        val orderedCases = results.keys.sortedBy { it.sorter() }
        val firstReport = reports[orderedCases.first()]!!
        orderedCases.forEach { case ->
            val report = reports[case]!!
            case.parent?.let {
                val parentReport = reports[it]!!
                parentReport.reports.add(report)
            }
        }

        return TestReport(name = className, reports = mutableListOf(firstReport))
    }
}
