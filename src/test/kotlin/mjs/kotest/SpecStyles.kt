/*
   Copyright 2022-2025 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package mjs.kotest

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.spec.style.WordSpec

fun randomTestResult() = listOf(
    ::passingTest,
    ::failingTest,
    ::errorTest,
).random().invoke()

class FunSpecExample : FunSpec({
    test("Using `FunSpec` (will randomly pass, fail or error)") {
        randomTestResult()
    }
})

class DescribeSpecExample : DescribeSpec({
    describe("Using `DescribeSpec`") {
        it("with `it` nested in `describe` (will randomly pass, fail or error)") {
            randomTestResult()
        }
    }
})

class ShouldSpecExample : ShouldSpec({
    should("Using `ShouldSpec` (will randomly pass, fail or error)") {
        randomTestResult()
    }
})

class StringSpecExample : StringSpec({
    "Using `StringSpec` (will randomly pass, fail or error)" {
        randomTestResult()
    }
})

class BehaviorSpecExample : BehaviorSpec({
    Given("Using `BehaviorSpec`") {
        When("the test runs") {
            Then("it passes or not (will randomly pass, fail or error)") {
                randomTestResult()
            }
        }
    }
})

class FreeSpecExample : FreeSpec({
    "Using `FreeSpec`" - {
        "can nest" - {
            "another level (will randomly pass, fail or error)" {
                randomTestResult()
            }
        }
    }
})

class WordSpecExample : WordSpec({
    "Using `WordSpec`" should {
        "pass or not (will randomly pass, fail or error)" {
            randomTestResult()
        }
    }
})

class FeatureSpecExample : FeatureSpec({
    feature("Using `FeatureSpec`") {
        scenario("with a scenario (will randomly pass, fail or error)") {
            randomTestResult()
        }
    }
})

class ExpectSpecExample : ExpectSpec({
    context("Using `ExpectSpec`") {
        expect("pass or not (will randomly pass, fail or error)") {
            randomTestResult()
        }
    }
})

class AnnotationSpecExample : AnnotationSpec() {
    @Test
    fun `Using AnnotationSpec (will randomly pass, fail or error)`() {
        randomTestResult()
    }
}
