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

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Write build reports to a directory relative to the build directory.
 */
internal object BuildReportWriter {

    private const val DefaultBuildDir = "./build"
    private const val GradleBuildDirKey = "gradle.build.dir"

    internal fun writeReportFile(outputDir: String, fileName: String, fileContents: String) {
        val path = outputDir(outputDir).resolve(fileName)
        if (path.parent.toFile().mkdirs()) {
            path.toFile().writeText(fileContents)
        }
    }

    private fun outputDir(outputDir: String): Path {
        val buildDir = System.getProperty(GradleBuildDirKey)
        return Paths.get(buildDir ?: DefaultBuildDir).resolve(outputDir)
    }
}
