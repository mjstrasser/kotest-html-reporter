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
            logger.info(
                "finalizeSpec(): {class} {numResults} {mapClass}",
                kclass.qualifiedName, results.size, results::class.qualifiedName
            )
            results.forEach { (case, result) ->
                val source = case.source as SourceRef.FileSource
                logger.info(
                    "{path}: {result} ({duration}) – {fileName}:{lineNumber}",
                    case.descriptor.path().value,
                    result.name,
                    result.duration,
                    source.fileName,
                    source.lineNumber,
                )
            }
            logger.info("----------")
        }
        delay(1000)
    }

}
