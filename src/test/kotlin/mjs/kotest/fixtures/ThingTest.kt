/*

   Copyright 2022 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package mjs.kotest.fixtures

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import mjs.kotest.description

class ThingTest : DescribeSpec({
    description(
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
