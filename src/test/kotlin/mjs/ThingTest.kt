package mjs

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ThingTest : DescribeSpec({
    describe("Describe the thing") {
        describe("the inner thing") {
            it("is thing") {
                "thing" shouldBe "thing"
            }
            it("is not thang") {
                "thing" shouldBe "thang"
            }
        }
        xit("don’t care") {
            "ignored" shouldBe "ignored"
        }
    }
})
