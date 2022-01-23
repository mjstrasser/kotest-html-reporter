package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.time.Duration

const val FAILURE = "Failure"
const val CHILD_FAILURE = "ChildFailure"

internal class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    companion object {
        const val DefaultBuildDir = "./build"
        const val GradleBuildDirKey = "gradle.build.dir"
    }

    private fun outputDir(): Path {
        val buildDir = System.getProperty(GradleBuildDirKey)
        return if (buildDir != null)
            Paths.get(buildDir).resolve(outputDir)
        else
            Paths.get(DefaultBuildDir).resolve(outputDir)
    }

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val className = kclass.qualifiedName!!
        val report = reportFromResults(className, results)
        writeReportJson(report, className)
    }

    private fun writeReportJson(report: TestReport, className: String) {
        val reportJson = Json.encodeToString(report)
        val path = outputDir().resolve("$className.json")
        path.parent.toFile().mkdirs()
        path.toFile().writeText(reportJson)
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
        val specReport = TestReport(className)
        orderedCases.forEach { case ->
            val report = reports[case]!!
            val parent = case.parent
            if (parent != null) {
                val parentReport = reports[parent]!!
                parentReport.addChildReport(report)
            } else
                specReport.addChildReport(report)
        }

        setChildFailuresOnContainers(specReport)

        return specReport
    }

    private fun setChildFailuresOnContainers(report: TestReport) {
        report.reports.forEach {
            setChildFailuresOnContainers(it)
            if (it.result == FAILURE || it.result == CHILD_FAILURE)
                report.result = CHILD_FAILURE
        }
    }
}

private const val SourceFormatter = "%s:%04d"
internal fun TestCase.sorter(): String = source.let {
    when (it) {
        is SourceRef.FileSource -> SourceFormatter.format(it.fileName, it.lineNumber)
        is SourceRef.ClassSource -> SourceFormatter.format(it.fqn, it.lineNumber)
        else -> "zzzzzz:0000"
    }
}
