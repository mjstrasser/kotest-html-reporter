package mjs.kotest

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MarkdownTest : FunSpec({

    description(
        """
        | This is an example of using the `description` extension property, that
        | can be used to describe the code being tested by a spec. 
        | 
        | You can put limited Markdown in the description and it will be converted to HTML.
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

    test("it ignores blank lines when converting double newlines") {
        "one\n     \ntwo".convertMarkdown() shouldBe "one<br><br>two"
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
