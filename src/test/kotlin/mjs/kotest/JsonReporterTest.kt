package mjs.kotest

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import mjs.kotest.fixtures.ThingTest
import mjs.kotest.fixtures.readTestReport
import mjs.kotest.fixtures.resultsMap
import java.io.File
import kotlin.io.path.createTempDirectory

class JsonReporterTest : DescribeSpec({
    describe("for ThingTest.kt") {
        it("creates expected JSON") {
            val tempDir = createTempDirectory("reporter-test")
            val reporter = JsonReporter(tempDir.toString())

            reporter.finalizeSpec(ThingTest::class, resultsMap)

            val outputFile = File(tempDir.toFile(), "test-report.json")
            val json = outputFile.readText()
            json shouldBe "{}"

            val expectedReport = readTestReport("expected/thing-test.json")
            val testReport = TestReport(
                "mjs.ThingTest",
                reports = listOf(
                    TestReport(
                        "0. Describe the thing",
                        "Failure",
                        "81.112632ms",
                        reports = listOf(
                            TestReport(
                                "1. I don’t care",
                                result = "Ignored",
                            ),
                            TestReport(
                                "2. the inner thing",
                                "Failure",
                                "34.051853ms",
                                reports = listOf(
                                    TestReport(
                                        "2a. is thing",
                                        "Success",
                                        "5.236817ms",
                                    ),
                                    TestReport(
                                        "2b. is not thang",
                                        "Failure",
                                        "17.143399ms",
                                        "io.kotest.assertions.AssertionFailedError: expected:<\"thang\"> but was:<\"thing\">\n\tat mjs.ThingTest\$1\$1\$1\$2.invokeSuspend(ThingTest.kt:14)\n\tat mjs.ThingTest\$1\$1\$1\$2.invoke(ThingTest.kt)\n"
                                    )
                                )
                            ),
                            TestReport(
                                "3. also ignored",
                                "Ignored"
                            )
                        )
                    )
                )
            )
            testReport shouldBe expectedReport
        }
    }
})
