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

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

/** Create an HTML report for every test run. */
object KotestConfig : AbstractProjectConfig() {
    override val extensions: List<Extension> = listOf(
        HtmlReporter(reportFilename = "index.html"),
    )
}
