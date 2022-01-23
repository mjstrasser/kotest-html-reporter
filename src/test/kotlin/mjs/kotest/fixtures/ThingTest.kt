package mjs.kotest.fixtures

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import mjs.kotest.preamble

class ThingTest : DescribeSpec({
    preamble(
        """
        Here is a slightly convoluted test class with some levels of containers.
        
        It also contains some ignored tests and one that fails.
    """.trimIndent()
    )
    describe("0. Describe the thing") {
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
        xdescribe("3. also ignored") {
            it("3a. is not relevant") {
                "ignored" shouldNotBe "ignored"
            }
        }
    }
    describe("4. Another root") {
        it("is really fun") {
            "fun" shouldBe "fun"
        }
    }
})
