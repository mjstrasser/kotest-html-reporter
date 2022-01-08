package mjs.kotest

data class TestReport(
    val testName: String,
    val result: String = "",
    val duration: String = "",
    val children: List<TestReport> = listOf(),
) {
    fun addChild(child: TestReport): TestReport = TestReport(
        testName, result, duration, children + child
    )

    fun addResult(result: String, duration: String): TestReport = TestReport(
        testName, result, duration, children
    )
}
