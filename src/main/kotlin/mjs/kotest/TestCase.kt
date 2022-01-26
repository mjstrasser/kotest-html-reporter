package mjs.kotest

import io.kotest.core.source.SourceRef
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase

private const val SourceFormatter = "%s:%04d"

/** For sorting test cases by source file/class and line. */
internal val TestCase.sourceAndLine: String
    get() = source.let {
        when (it) {
            is SourceRef.FileSource -> SourceFormatter.format(it.fileName, it.lineNumber)
            is SourceRef.ClassSource -> SourceFormatter.format(it.fqn, it.lineNumber)
            else -> "zzzzzz:0000"
        }
    }

/** Name for reporting that may include prefix and suffix. */
internal val TestCase.reportingName: String
    get() = when (spec) {
        is BehaviorSpec -> with(name) { (prefix ?: "") + testName + (suffix ?: "") }
        else -> name.testName
    }
