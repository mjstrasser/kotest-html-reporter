package mjs.kotest.fixtures

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class BehaviorSpecTest : BehaviorSpec({
    Given("The usual setup") {
        When("I do some really cool thing") {
            Then("It all works") {
                "it" shouldBe "it"
            }
        }
        When("I do some other thing") {
            Then("it works as well") {
                "also" shouldBe "also"
            }
        }
    }
})
