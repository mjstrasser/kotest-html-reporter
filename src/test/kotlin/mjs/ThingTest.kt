package mjs

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ThingTest : DescribeSpec({
    describe("Describe the thing") {
        xit("1. don’t care") {
            "ignored" shouldBe "ignored"
        }
        describe("2. the inner thing") {
            it("2a. is thing") {
                "thing" shouldBe "thing"
            }
            it("2b. is not thang") {
                "thing" shouldBe "thang"
            }
        }
    }
})
