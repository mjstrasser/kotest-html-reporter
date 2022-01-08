package mjs.kotest

import io.klogging.Klogging
import io.kotest.core.listeners.AfterContainerListener
import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeContainerListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.isRootTest

class JsonReporter(
    private val outputDir: String = "reports/kotest",
) : BeforeContainerListener, BeforeEachListener, AfterContainerListener, AfterEachListener, AfterSpecListener,
    Klogging {

    private val reports: MutableMap<TestCase, TestReport> = mutableMapOf()

    override suspend fun beforeContainer(testCase: TestCase) {
        logger.info("beforeContainer(): {name}", testCase.name.testName)
        addTestCase(testCase)
    }

    private suspend fun addTestCase(testCase: TestCase) {
        val report = TestReport(testCase.name.testName)
        reports[testCase] = report
        if (testCase.isRootTest()) return
        reports[testCase.parent]?.let { parent ->
            reports[testCase.parent!!] = parent.addChild(report)
        } ?: logger.warn("Non-root case parent not found")
    }

    override suspend fun beforeEach(testCase: TestCase) {
        logger.info("beforeEach(): {name}", testCase.name.testName)
        addTestCase(testCase)
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        logger.info(
            "afterContainer(): {name}: parent={parent} {result} ({duration})",
            testCase.name.testName,
            testCase.parent?.name?.testName,
            result.name,
            result.duration
        )
        reports[testCase]?.let { report ->
            reports[testCase] = report.addResult("", result.duration.toString())
        } ?: logger.warn("afterContainer(): no report found for ${testCase.descriptor.id}")
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        logger.info(
            "afterEach(): {name}: parent={parent} ({duration})",
            testCase.name.testName,
            testCase.parent?.name?.testName,
            result.duration
        )
        reports[testCase]?.let { report ->
            reports[testCase] = report.addResult(result.name, result.duration.toString())
        } ?: logger.warn("afterEach(): no report found for ${testCase.descriptor.id}")
    }

    override suspend fun afterSpec(spec: Spec) {
        reports.forEach { println("afterSpec(): $it") }
    }
}
