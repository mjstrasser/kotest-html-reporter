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
        val report = reportFromResults(results)
//        val jsonFile = File(File(outputDir), "test-report.json")
//        jsonFile.appendText("{}")
        delay(1000)
    }

    private fun durationIfPositive(result: TestResult): String? =
        if (result.duration > Duration.ZERO) result.duration.toString()
        else null

    fun reportFromResults(results: Map<TestCase, TestResult>): TestReport? {

        val reports = results.mapValues { (case, result) ->
            TestReport(
                case.name.testName,
                if (case.isRootTest()) null else result.name,
                durationIfPositive(result),
                result.errorOrNull?.message,
            )
        }.toMutableMap()

        val orderedCases = reports.keys.sortedByDescending { it.sorter() }
        orderedCases.forEach { case ->
            val parentCase = case.parent
            if (parentCase != null) {
                reports[case]?.let { thisReport ->
                    reports[case.parent]?.let { parentReport ->
                        reports[parentCase] = parentReport.prependChild(thisReport)
                    }
                }
            }
        }

        return reports[reports.keys.first { it.isRootTest() }]
    }
}
