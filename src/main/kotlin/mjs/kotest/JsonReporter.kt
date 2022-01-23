package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.time.Duration

class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    companion object {
        const val defaultBuildDir = "./build"
        const val gradleBuildDirKey = "gradle.build.dir"
    }

    private fun outputDir(): Path {
        val buildDir = System.getProperty(gradleBuildDirKey)
        return if (buildDir != null)
            Paths.get(buildDir).resolve(outputDir)
        else
            Paths.get(defaultBuildDir).resolve(outputDir)
    }

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val className = kclass.qualifiedName!!
        val report = reportFromResults(className, results)
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
        val specRootReports = mutableListOf<TestReport>()
        orderedCases.forEach { case ->
            val report = reports[case]!!
            case.parent?.let {
                val parentReport = reports[it]!!
                parentReport.reports.add(report)
            } ?: specRootReports.add(report)
        }

        return TestReport(name = className, reports = specRootReports)
    }
}

const val SourceFormatter = "%s:%04d"
internal fun TestCase.sorter(): String = source.let {
    when (it) {
        is SourceRef.FileSource -> SourceFormatter.format(it.fileName, it.lineNumber)
        is SourceRef.ClassSource -> SourceFormatter.format(it.fqn, it.lineNumber)
        else -> "zzzzzz:0000"
    }
}
