package mjs.kotest

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import mjs.kotest.fixtures.ThingTest
import mjs.kotest.fixtures.case0
import mjs.kotest.fixtures.case1
import mjs.kotest.fixtures.case2
import mjs.kotest.fixtures.case2a
import mjs.kotest.fixtures.case2b
import mjs.kotest.fixtures.case3
import mjs.kotest.fixtures.readTestReport
import mjs.kotest.fixtures.resultsMap
import java.io.File
import kotlin.io.path.createTempDirectory

class JsonReporterTest : DescribeSpec({
    describe("for ThingTest.kt") {
        val testReport = TestReport(
            "mjs.ThingTest",
            reports = mutableListOf(
                TestReport(
                    "0. Describe the thing",
                    "ChildFailure",
                    "81.112632ms",
                    reports = mutableListOf(
                        TestReport(
                            "1. don’t care",
                            result = "Ignored",
                        ),
                        TestReport(
                            "2. the inner thing",
                            "ChildFailure",
                            "34.051853ms",
                            reports = mutableListOf(
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
        it("creates expected JSON") {
            val tempDir = createTempDirectory("reporter-test")
            val reporter = JsonReporter(tempDir.toString())

            reporter.finalizeSpec(ThingTest::class, resultsMap)

            val outputFile = File(tempDir.toFile(), "test-report.json")
            val json = outputFile.readText()
            json shouldBe "{}"

            val expectedReport = readTestReport("expected/thing-test.json")
            testReport shouldBe expectedReport
        }
        it("creates expected report") {
            val report = JsonReporter().reportFromResults("mjs.ThingTest", resultsMap)
            val json = report.toJson()
            json shouldBe testReport.toJson()
        }
    }
    describe("sorting test cases") {
        it("should work") {
            setOf(case0, case1, case2, case3, case2a, case2b)
                .shuffled()
                .sortedBy { it.sorter() } shouldBe
                    listOf(case0, case1, case2, case2a, case2b, case3)
        }
    }
})
