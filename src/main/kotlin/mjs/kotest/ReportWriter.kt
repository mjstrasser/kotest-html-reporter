package mjs.kotest

import java.nio.file.Path
import java.nio.file.Paths

internal object ReportWriter {

    private const val DefaultBuildDir = "./build"
    private const val GradleBuildDirKey = "gradle.build.dir"

    internal fun writeReportFile(outputDir: String, fileName: String, fileContents: String) {
        val path = outputDir(outputDir).resolve(fileName)
        path.parent.toFile().mkdirs()
        path.toFile().writeText(fileContents)
    }

    private fun outputDir(outputDir: String): Path {
        val buildDir = System.getProperty(GradleBuildDirKey)
        return if (buildDir != null)
            Paths.get(buildDir).resolve(outputDir)
        else
            Paths.get(DefaultBuildDir).resolve(outputDir)
    }
}