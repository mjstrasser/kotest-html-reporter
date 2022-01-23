package mjs.kotest

import io.kotest.core.spec.Spec

private val SpecPreambles: MutableMap<Spec, String> = mutableMapOf()

internal fun Spec.preamble(specPreamble: String) {
    SpecPreambles[this] = specPreamble
}

internal val Spec.preamble: String?
    get() = SpecPreambles[this]
