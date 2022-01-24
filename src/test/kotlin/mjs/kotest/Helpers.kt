package mjs.kotest

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.random.Random
import kotlin.random.nextUInt

/** Perhaps select from dictionary later. */
fun randomWord(): String = Random.nextUInt().toString(16)

fun randomName(maxWords: Int = 10): String =
    Array(Random.nextInt(1, maxWords + 1)) { randomWord() }.toList().joinToString(" ")

fun passingTest() {
    val word = randomWord()
    word shouldBe word
}

fun failingTest() {
    val word = randomWord()
    word shouldNotBe word
}
