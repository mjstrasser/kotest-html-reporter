package mjs

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay

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
        delay(1000)
    }
})
