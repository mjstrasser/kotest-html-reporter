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

import mjs.kotest.building.configurePublishing
import mjs.kotest.building.configureSpotless
import mjs.kotest.building.configureTesting
import mjs.kotest.building.configureVersioning
import mjs.kotest.building.configureWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`
}

group = "mjs.kotest"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinSerialisationJsonVersion = "1.3.2"
val kotestVersion = "5.1.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialisationJsonVersion")
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val ktlintVersion: String by project

configurePublishing()
configureSpotless(ktlintVersion)
configureTesting()
configureVersioning()
configureWrapper()
