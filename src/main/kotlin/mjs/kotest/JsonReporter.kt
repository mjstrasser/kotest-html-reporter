package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.delay
import kotlin.reflect.KClass

class JsonReporter(
//    private val outputDir: String = "reports/kotest",
) : FinalizeSpecListener, Klogging {

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        logger.info(
            "finalizeSpec(): {class} {numResults}",
            kclass.simpleName, results.size
        )
        results.forEach { (case, result) ->
            logger.info(
                "{path}: {result} ({duration})",
                case.descriptor.path(),
                result.name,
                result.duration
            )
        }
        delay(1000)
    }

}
