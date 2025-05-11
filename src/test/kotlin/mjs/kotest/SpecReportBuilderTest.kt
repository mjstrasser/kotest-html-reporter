/*
   Copyright 2022-2025 Michael Strasser.

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
package mjs.kotest

import io.kotest.core.descriptors.Descriptor
import io.kotest.core.descriptors.DescriptorId
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import mjs.kotest.SpecReportBuilder.reportFromResults
import kotlin.time.Duration

class SpecReportBuilderTest : DescribeSpec({
    val thisSpec = this
    describe("`reportFromResults()` function") {
        it("converts `Error` result to `Failure`") {
            val specDescriptor = Descriptor.SpecDescriptor(DescriptorId("SpecReportBuilderTest.kt"), thisSpec::class)
            val name = "error result"
            val descriptor = Descriptor.TestDescriptor(specDescriptor, DescriptorId(name))
            val case = TestCase(descriptor, TestName(name), thisSpec, {}, type = TestType.Test)
            val errorResult = TestResult.Error(Duration.parse("16.342524ms"), NullPointerException("Oh noes!"))

            val report = reportFromResults(randomWord(), mapOf(case to errorResult))

            report.reports shouldHaveSize 1
            report.reports[0].result shouldBe "Failure"
        }
    }
})
