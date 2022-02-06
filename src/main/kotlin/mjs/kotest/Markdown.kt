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

private val paraRegex = Regex("\n *\n")
private val codeRegex = Regex("`([^`]+)`")
private val italicRegex = Regex("_(\\S[^_]+)_")
private val boldRegex = Regex("\\*\\*(\\S[^_]+)\\*\\*")

private fun String.para() = paraRegex.replace(this, "<br><br>")
private fun String.code() = codeRegex.replace(this, "<span class='code'>\$1</span>")
private fun String.italic() = italicRegex.replace(this, "<em>\$1</em>")
private fun String.bold() = boldRegex.replace(this, "<strong>\$1</strong>")

internal fun String.convertMarkdown(): String = this.para().code().italic().bold()
