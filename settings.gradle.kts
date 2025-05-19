/*

   Copyright 2022 Michael Strasser.

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

rootProject.name = "kotest-html-reporter"

// Setup so builds succeed when a new version of Kotlin has only partly propagated to repositories.
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}


// Reckon plugin to set version based on Git tags.
plugins {
    id("org.ajoberstar.reckon.settings") version "0.19.2"
}
extensions.configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
    setDefaultInferredScope("minor")
    snapshots()
    setStageCalc(calcStageFromProp())
    setScopeCalc(calcScopeFromProp())
}
