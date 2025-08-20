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
package mjs.kotest.fixtures

import io.kotest.assertions.AssertionFailedError
import io.kotest.core.descriptors.Descriptor
import io.kotest.core.descriptors.DescriptorId
import io.kotest.core.names.TestName
import io.kotest.core.source.SourceRef
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import kotlin.time.Duration

val specDescriptor = Descriptor.SpecDescriptor(DescriptorId(ThingTest::class.qualifiedName!!))
val thingTest = ThingTest()

const val NAME_0 = "0. Describe the thing"
val descriptor0 = Descriptor.TestDescriptor(specDescriptor, DescriptorId(NAME_0))
val case0 =
    TestCase(
        descriptor0,
        TestName(NAME_0, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 8),
        TestType.Container,
        parent = null,
    )
val result0 = TestResult.Success(Duration.parse("81.112632ms"))

const val NAME_1 = "1. I don’t care"
val descriptor1 = Descriptor.TestDescriptor(descriptor0, DescriptorId(NAME_1))
val case1 =
    TestCase(
        descriptor1,
        TestName(NAME_1, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 9),
        TestType.Test,
        parent = case0,
    )
val result1 = TestResult.Ignored("Disabled by xmethod")

const val NAME_2 = "2. the inner thing"
val descriptor2 = Descriptor.TestDescriptor(descriptor0, DescriptorId(NAME_2))
val case2 =
    TestCase(
        descriptor2,
        TestName(NAME_2, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 12),
        TestType.Container,
        parent = case0,
    )
val result2 = TestResult.Success(Duration.parse("34.051853ms"))

const val NAME_2A = "2a. is thing"
val descriptor2a = Descriptor.TestDescriptor(descriptor2, DescriptorId(NAME_2A))
val case2a =
    TestCase(
        descriptor2a,
        TestName(NAME_2A, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 13),
        TestType.Test,
        parent = case2,
    )
val result2a = TestResult.Success(Duration.parse("5.236817ms"))

const val NAME_2B = "2b. is not thang"
val descriptor2b = Descriptor.TestDescriptor(descriptor2, DescriptorId(NAME_2B))
val case2b =
    TestCase(
        descriptor2b,
        TestName(NAME_2B, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 16),
        TestType.Test,
        parent = case2,
    )
val result2b =
    TestResult.Failure(
        Duration.parse("17.143399ms"),
        AssertionFailedError("expected:<\"thang\"> but was:<\"thing\">", null, "thang", "thing"),
    )

const val NAME_3 = "3. also ignored"
val descriptor3 = Descriptor.TestDescriptor(descriptor0, DescriptorId(NAME_3))
val case3 =
    TestCase(
        descriptor3,
        TestName(NAME_3, false, false, null, null, false),
        thingTest,
        {},
        SourceRef.ClassSource(ThingTest::class.java.name, 20),
        TestType.Container,
        parent = case0,
    )
val result3 = TestResult.Ignored("Disabled by xmethod")

val resultsMap: Map<TestCase, TestResult> =
    mapOf(
        case0 to result0,
        case1 to result1,
        case2 to result2,
        case2a to result2a,
        case2b to result2b,
        case3 to result3,
    )
