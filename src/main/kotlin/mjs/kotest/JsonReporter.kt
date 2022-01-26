package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import mjs.kotest.ReportBuilder.reportFromResults
import mjs.kotest.ReportWriter.writeReportFile
import kotlin.reflect.KClass

internal class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val className = kclass.qualifiedName!!
        val report = reportFromResults(className, results)
        writeReportFile(outputDir, "$className.json", report.toJson())
    }

}
