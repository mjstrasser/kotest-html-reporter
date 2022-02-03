package mjs.kotest

private val codeRegex = Regex("`([^`]+)`")
private val italicRegex = Regex("_(\\S[^_]+)_")
private val boldRegex = Regex("\\*\\*(\\S[^_]+)\\*\\*")

private fun String.para() = this.replace("\n\n", "<br><br>")
private fun String.code() = codeRegex.replace(this, "<span class='code'>\$1</span>")
private fun String.italic() = italicRegex.replace(this, "<em>\$1</em>")
private fun String.bold() = boldRegex.replace(this, "<strong>\$1</strong>")

internal fun String.convertMarkdown(): String = this.para().code().italic().bold()
