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

/**
 * A report that can have child reports.
 *
 * Reports use mutable fields for speed and convenience.
 */
internal interface Report {
    val result: String?
    val reports: MutableList<TestReport>
    fun addChildReport(childReport: TestReport) {
        reports.add(childReport)
    }
}

/** Report for a test case. */
internal data class TestReport(
    val name: String,
    override var result: String? = null,
    val duration: String? = null,
    val message: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report

/** Report for a spec. */
internal data class SpecReport(
    val name: String,
    var description: String? = null,
    override val reports: MutableList<TestReport> = mutableListOf(),
) : Report {
    override val result: String
        get() = reports.fold("Success") { res, rep ->
            if (rep.result != "Success") "ChildFailure" else res
        }
}
