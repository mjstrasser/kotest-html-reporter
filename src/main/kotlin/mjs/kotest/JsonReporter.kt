package mjs.kotest

import io.klogging.Klogging
import io.klogging.context.logContext
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.reflect.KClass

class JsonReporter(
//    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    val run = UUID.randomUUID()

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        withContext(logContext("run" to run)) {
            logger.info("----------")
            logger.info(
                "finalizeSpec(): {class} {numResults} {mapClass}",
                kclass.qualifiedName, results.size, results::class.qualifiedName
            )
            results.forEach { (case, result) ->
                println("-- ${case.name.testName} --")
                println(case)
                println(result)
                val source = nameAndNumber(case.source)
                logger.info(
                    "{path}: {result} ({duration}) – {name}:{lineNumber}",
                    case.descriptor.path().value,
                    result.name,
                    result.duration,
                    source.first,
                    source.second,
                )
            }
        }
        delay(1000)
    }

    private fun nameAndNumber(sourceRef: SourceRef): Pair<String, Int?> = when (sourceRef) {
        is SourceRef.FileSource -> Pair(sourceRef.fileName, sourceRef.lineNumber)
        is SourceRef.ClassSource -> Pair(sourceRef.fqn, sourceRef.lineNumber)
        else -> Pair("?", null)
    }

}
