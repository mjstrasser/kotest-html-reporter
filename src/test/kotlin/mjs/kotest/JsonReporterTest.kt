package mjs.kotest

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import mjs.kotest.fixtures.ThingTest
import mjs.kotest.fixtures.resultsMap
import java.io.File
import kotlin.io.path.createTempDirectory

class JsonReporterTest : DescribeSpec({
    describe("for ThingTest.kt") {
        it("creates expected JSON") {
            val tempDir = createTempDirectory("reporter-test")
            val reporter = JsonReporter(tempDir.toString())

            reporter.finalizeSpec(ThingTest::class, resultsMap)

            val outputFile = File(tempDir.toFile(), "output.json")
            val json = outputFile.readText()

            json shouldBe "{}"
        }
    }
})
