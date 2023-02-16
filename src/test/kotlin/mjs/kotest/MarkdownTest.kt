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

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MarkdownTest : FunSpec({

    description(
        """
        | This is an example of using the `description` extension property, that
        | can be used to describe the code being tested by a spec. 
        | 
        | You can put limited Markdown in the description and it will be converted to HTML. Double
        | newlines are converted into double line breaks.
        | 
        | - It converts `code in backticks`.
        | 
        | - It converts **bold** text.
        | 
        | - It converts _italic_ text.
        | 
        | (It does not convert lists.)
    """.trimMargin()
    )

    test("it converts double newlines into double line breaks") {
        "one\n\ntwo".convertMarkdown() shouldBe "one<br><br>two"
    }
    test("it ignores blank lines with spaces and/or tabs when converting double newlines") {
        "one\n  \t   \ntwo".convertMarkdown() shouldBe "one<br><br>two"
    }
    test("it ignores single newlines") {
        "one\ntwo".convertMarkdown() shouldBe "one\ntwo"
    }
    test("it converts code at the start of strings") {
        "`one` and two".convertMarkdown() shouldBe "<span class='code'>one</span> and two"
    }
    test("it converts code in the middle of strings") {
        "one `and` two".convertMarkdown() shouldBe "one <span class='code'>and</span> two"
    }
    test("it converts code at the end of strings") {
        "one and `two`".convertMarkdown() shouldBe "one and <span class='code'>two</span>"
    }
    test("it converts multi-word code") {
        "one `and two`".convertMarkdown() shouldBe "one <span class='code'>and two</span>"
    }
    test("it converts multi-line code") {
        "one `and\n\ntwo`".convertMarkdown() shouldBe "one <span class='code'>and<br><br>two</span>"
    }
})
