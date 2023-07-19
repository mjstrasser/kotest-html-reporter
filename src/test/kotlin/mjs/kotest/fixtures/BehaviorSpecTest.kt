/*
   Copyright 2022-2023 Michael Strasser.

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

import io.kotest.core.spec.style.BehaviorSpec
import mjs.kotest.errorTest
import mjs.kotest.failingTest
import mjs.kotest.fixtures.BehaviorSpecFixture.givenName
import mjs.kotest.fixtures.BehaviorSpecFixture.thenName1
import mjs.kotest.fixtures.BehaviorSpecFixture.thenName2
import mjs.kotest.fixtures.BehaviorSpecFixture.thenName3
import mjs.kotest.fixtures.BehaviorSpecFixture.whenName1
import mjs.kotest.fixtures.BehaviorSpecFixture.whenName2
import mjs.kotest.fixtures.BehaviorSpecFixture.whenName3
import mjs.kotest.passingTest
import mjs.kotest.randomName

class BehaviorSpecTest : BehaviorSpec({
    Given(givenName) {
        When(whenName1) {
            Then(thenName1) {
                passingTest()
            }
        }
        When("$whenName2 (will fail)") {
            Then(thenName2) {
                failingTest()
            }
        }
        When("$whenName3 (will fail because exception thrown)") {
            Then(thenName3) {
                errorTest()
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
    val whenName3 = randomName()
    val thenName3 = randomName()
}
