package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.descriptors.TestPath
import io.kotest.core.listeners.AfterContainerListener
import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeContainerListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.isRootTest
import kotlinx.coroutines.delay

class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : BeforeContainerListener, BeforeEachListener,
    AfterContainerListener, AfterEachListener, AfterSpecListener,
    Klogging {

    private val reports: MutableMap<TestPath, TestReport> = mutableMapOf()

    override suspend fun beforeContainer(testCase: TestCase) {
        logger.info("beforeContainer(): {path}", testCase.descriptor.path().value)
        addTestCase(testCase)
    }

    override suspend fun beforeEach(testCase: TestCase) {
        logger.info("beforeEach(): {path}", testCase.descriptor.path().value)
        addTestCase(testCase)
    }

    private suspend fun addTestCase(testCase: TestCase) {
        val path = testCase.descriptor.path()
        val report = TestReport(path.value, testCase.name.testName)
        reports[path] = report
        if (testCase.isRootTest()) return
        reports[testCase.parent?.descriptor?.path()]?.let { parent ->
            reports[testCase.parent!!.descriptor.path()] = parent.addChild(report)
        } ?: logger.warn("Non-root case parent not found")
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        val path = testCase.descriptor.path()
        logger.info(
            "afterContainer(): {path}: {result} ({duration})",
            path.value,
            result.name,
            result.duration
        )
        reports[path]?.let { report ->
            reports[path] = report.addResult(result.name, result.duration.toString())
        } ?: logger.warn("afterContainer(): no report found for {path}", path)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        val path = testCase.descriptor.path()
        logger.info(
            "afterEach(): {path}: {result} ({duration})",
            path.value,
            result.name,
            result.duration
        )
        reports[path]?.let { report ->
            reports[path] = report.addResult(result.name, result.duration.toString())
        } ?: logger.warn("afterEach(): no report found for {path}", path)
    }

    override suspend fun afterSpec(spec: Spec) {
        reports.forEach { logger.info("afterSpec(): {report}", it.value) }
        delay(500)
    }
}
