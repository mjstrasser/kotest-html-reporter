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

val specDescriptor = Descriptor.SpecDescriptor(DescriptorId(ThingTest::class.qualifiedName!!), ThingTest::class)
val thingTest = ThingTest()

const val name0 = "0. Describe the thing"
val descriptor0 = Descriptor.TestDescriptor(specDescriptor, DescriptorId(name0))
val case0 = TestCase(
    descriptor0, TestName(name0), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 8),
    TestType.Container, parent = null
)
val result0 = TestResult.Success(Duration.parse("81.112632ms"))

const val name1 = "1. don’t care"
val descriptor1 = Descriptor.TestDescriptor(descriptor0, DescriptorId(name1))
val case1 = TestCase(
    descriptor1, TestName(name1), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 9),
    TestType.Test, parent = case0
)
val result1 = TestResult.Ignored("Disabled by xmethod")

const val name2 = "2. the inner thing"
val descriptor2 = Descriptor.TestDescriptor(descriptor0, DescriptorId(name2))
val case2 = TestCase(
    descriptor2, TestName(name2), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 12),
    TestType.Test, parent = case1
)
val result2 = TestResult.Success(Duration.parse("34.051853ms"))

const val name2a = "2a. is thing"
val descriptor2a = Descriptor.TestDescriptor(descriptor2, DescriptorId(name2a))
val case2a = TestCase(
    descriptor2a, TestName(name2a), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 13),
    TestType.Test, parent = case2
)
val result2a = TestResult.Success(Duration.parse("5.236817ms"))

const val name2b = "2b. is not thang"
val descriptor2b = Descriptor.TestDescriptor(descriptor2, DescriptorId(name2b))
val case2b = TestCase(
    descriptor2b, TestName(name2b), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 16),
    TestType.Test, parent = case2
)
val result2b = TestResult.Failure(
    Duration.parse("17.143399ms"),
    AssertionFailedError("expected:<\"thang\"> but was:<\"thing\">", null, "thang", "thing"),
)

const val name3 = "3. also ignored"
val descriptor3 = Descriptor.TestDescriptor(descriptor0, DescriptorId(name3))
val case3 = TestCase(
    descriptor3, TestName(name3), thingTest, {}, SourceRef.FileSource("ThingTest.kt", 20),
    TestType.Test, parent = case0
)
val result3 = TestResult.Ignored("Disabled by xmethod")

val resultsMap: Map<TestCase, TestResult> = mapOf(
    case0 to result0,
    case1 to result1,
    case2 to result2,
    case2a to result2a,
    case2b to result2b,
    case3 to result3,
)
