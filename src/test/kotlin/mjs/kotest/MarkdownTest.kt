package mjs.kotest

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MarkdownTest : FunSpec({

    description("""Extension property that converts inline Markdown to HTML.
        |
        |This description contains _actual_ examples.
        |
        |**Yes** it really does.
    """.trimMargin())

    test("converts double newlines into double line breaks") {
        "one\n\ntwo".convertMarkdown() shouldBe "one<br><br>two"
    }

    test("converts code at the start of strings") {
        "`one` and two".convertMarkdown() shouldBe "<span class='code'>one</span> and two"
    }
    test("converts code in the middle of strings") {
        "one `and` two".convertMarkdown() shouldBe "one <span class='code'>and</span> two"
    }
    test("converts code at the end of strings") {
        "one and `two`".convertMarkdown() shouldBe "one and <span class='code'>two</span>"
    }
    test("converts multi-word code") {
        "one `and two`".convertMarkdown() shouldBe "one <span class='code'>and two</span>"
    }
    test("converts multi-line code") {
        "one `and\n\ntwo`".convertMarkdown() shouldBe "one <span class='code'>and<br><br>two</span>"
    }

})
