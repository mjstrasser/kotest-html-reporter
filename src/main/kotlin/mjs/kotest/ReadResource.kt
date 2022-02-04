package mjs.kotest

object ReadResource {
    internal fun readResourceText(resourcePath: String): String? =
        ClassLoader.getSystemClassLoader()
            .getResourceAsStream(resourcePath)
            ?.bufferedReader(Charsets.UTF_8)
            ?.readText()
}