package mjs.kotest

data class TestReport(
    val path: String,
    val testName: String,
    val result: String = "",
    val duration: String = "",
    val children: List<String> = listOf(),
) {
    fun addChild(child: TestReport): TestReport = TestReport(
        path, testName, result, duration, children + child.path
    )

    fun addResult(result: String, duration: String): TestReport = TestReport(
        path, testName, result, duration, children
    )
}
