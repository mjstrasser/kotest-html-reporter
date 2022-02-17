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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`
    signing
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "com.michaelstrasser"
version = "0.4.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
}

val kotlinSerialisationJsonVersion = "1.3.2"
val kotestVersion = "5.1.0"
val kotlinHtmlVersion = "0.7.3"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialisationJsonVersion")
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
}

tasks.test {
    useJUnitPlatform()
    // For now, because we have failing tests so they can be reported.
    ignoreFailures = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("dokkaJar") {
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("dokka/html"))
}

publishing {
    publications {
        create<MavenPublication>("kotlinLibrary") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["dokkaJar"])
            pom {
                name.set("kotes-html-reporter")
                description.set("Kotest plugin to create HTML reports of test runs")
                url.set("https://github.com/mjstrasser/kotest-html-reporter")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                    developers {
                        developer {
                            id.set("mjstrasser")
                            name.set("Michael Strasser")
                            email.set("kotest-html-reporter@michaelstrasser.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/mjstrasser/kotest-html-reporter.git")
                        url.set("https://github.com/mjstrasser/kotest-html-reporter")
                    }
                }
            }
        }
    }
}

nexusPublishing {
    val ossrhUsername: String? by project
    val ossrhPassword: String? by project
    repositories {
        create("sonatype") {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["kotlinLibrary"])
}
