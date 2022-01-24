package mjs.kotest.fixtures

import io.kotest.core.spec.style.BehaviorSpec
import mjs.kotest.SpecReport
import mjs.kotest.TestReport
import mjs.kotest.fixtures.BehaviorSpecFixture.givenName
import mjs.kotest.fixtures.BehaviorSpecFixture.thenName1
import mjs.kotest.fixtures.BehaviorSpecFixture.thenName2
import mjs.kotest.fixtures.BehaviorSpecFixture.whenName1
import mjs.kotest.fixtures.BehaviorSpecFixture.whenName2
import mjs.kotest.passingTest
import mjs.kotest.randomName

class BehaviorSpecTest : BehaviorSpec({
    Given(givenName) {
        When(whenName1) {
            Then(thenName1) {
                passingTest()
            }
        }
        When(whenName2) {
            Then(thenName2) {
                passingTest()
            }
        }
    }
})

internal object BehaviorSpecFixture {
    val givenName = randomName()
    val whenName1 = randomName()
    val thenName1 = randomName()
    val whenName2 = randomName()
    val thenName2 = randomName()

    val behaviorExpectedReport: SpecReport = SpecReport(
        "mjs.kotest.fixtures.BehaviorSpecTest",
        reports = mutableListOf(
            TestReport(
                "Given: $givenName",
                reports = mutableListOf(
                    TestReport("When: $whenName1", reports = mutableListOf(TestReport("Then: $thenName1"))),
                    TestReport("When: $whenName2", reports = mutableListOf(TestReport("Then: $thenName2"))),
                )
            )
        )
    )
}

