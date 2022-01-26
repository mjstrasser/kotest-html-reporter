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
