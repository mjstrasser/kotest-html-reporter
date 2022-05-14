package mjs.kotest

import io.kotest.core.descriptors.Descriptor
import io.kotest.core.descriptors.DescriptorId
import io.kotest.core.names.TestName
import io.kotest.core.source.SourceRef
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestType
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TestCaseTest : DescribeSpec() {
    init {
        describe("`TestCase.sourceAndLine` extension property") {
            it("returns by filename first") {
                Fixture.case1.sourceAndLine shouldBe "${Fixture.filename}:000${Fixture.firstLine}:${Fixture.firstName}"
            }
            it("returns by line number second") {
                Fixture.case2.sourceAndLine shouldBe "${Fixture.filename}:000${Fixture.secondLine}:${Fixture.secondName}"
            }
        }
        describe("for data-driven testing") {
            withData(listOf(Pair(1, 2), Pair(3, 4), Pair(0, 1))) { pair ->
                (pair.first + 1) shouldBe pair.second
            }
            it("returns test name third") {
                Fixture.case3.sourceAndLine shouldBe "${Fixture.filename}:000${Fixture.thirdLine}:${Fixture.thirdName}"
                Fixture.case4.sourceAndLine shouldBe "${Fixture.filename}:000${Fixture.fourthLine}:${Fixture.fourthName}"
            }
        }
    }

    private object Fixture {
        const val filename = "TestCaseTest.kt"
        const val firstName = "returns by filename first"
        const val firstLine = 15
        const val secondName = "returns by line number second"
        const val secondLine = 18
        const val thirdName = "(1, 2)"
        const val thirdLine = 24
        const val fourthName = "(3, 4)"
        const val fourthLine = 24

        private val testClass = TestCaseTest::class
        private val specDescriptor = Descriptor.SpecDescriptor(DescriptorId(testClass.qualifiedName!!), testClass)
        private val firstTestCaseTest = TestCaseTest()

        val case1 = TestCase(
            Descriptor.TestDescriptor(specDescriptor, DescriptorId(firstName)),
            TestName(firstName),
            firstTestCaseTest,
            {},
            SourceRef.FileSource(filename, firstLine),
            TestType.Container,
            parent = null
        )
        val case2 = TestCase(
            Descriptor.TestDescriptor(specDescriptor, DescriptorId(secondName)),
            TestName(secondName),
            firstTestCaseTest,
            {},
            SourceRef.FileSource(filename, secondLine),
            TestType.Container,
            parent = null
        )
        val case3 = TestCase(
            Descriptor.TestDescriptor(specDescriptor, DescriptorId(thirdName)),
            TestName(thirdName),
            firstTestCaseTest,
            {},
            SourceRef.FileSource(filename, thirdLine),
            TestType.Container,
            parent = null
        )
        val case4 = TestCase(
            Descriptor.TestDescriptor(specDescriptor, DescriptorId(fourthName)),
            TestName(fourthName),
            firstTestCaseTest,
            {},
            SourceRef.FileSource(filename, fourthLine),
            TestType.Container,
            parent = null
        )
    }
}
