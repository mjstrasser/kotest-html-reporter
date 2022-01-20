package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import java.io.File
import kotlin.reflect.KClass

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
        val jsonFile = File(File(outputDir), "test-report.json")
        jsonFile.appendText("{}")
    }

    fun reportFromResults(results: Map<TestCase, TestResult>): TestReport? {
        val orderedCases = results.keys.sortedBy { it.sorter() }

        return null
    }
}
