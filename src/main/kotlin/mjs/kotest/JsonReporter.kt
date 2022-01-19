package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import java.io.File
import kotlin.reflect.KClass

class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        val jsonFile = File(File(outputDir), "output.json")
        jsonFile.appendText("{}")
    }

    private fun nameAndNumber(sourceRef: SourceRef): Pair<String, Int?> = when (sourceRef) {
        is SourceRef.FileSource -> Pair(sourceRef.fileName, sourceRef.lineNumber)
        is SourceRef.ClassSource -> Pair(sourceRef.fqn, sourceRef.lineNumber)
        else -> Pair("?", null)
    }

}
