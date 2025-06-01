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

import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    alias(libs.plugins.serialisation)
    `java-library`
    alias(libs.plugins.vanniktech.publish)
    alias(libs.plugins.versions)
    alias(libs.plugins.spotless)
}

group = "com.michaelstrasser"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.kotlin.html)
    implementation(libs.kotest.runner)
}

tasks.test {
    useJUnitPlatform()
    // Because we have failing tests so they can be reported.
    ignoreFailures = true
}

val kotlinLicenseHeader = """/*
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
"""

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint(libs.versions.ktlint.get())

        licenseHeader(kotlinLicenseHeader)

        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("dokkaJar") {
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("dokka/html"))
}

mavenPublishing {
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
        )
    )

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)

    signAllPublications()

    coordinates("com.michaelstrasser", "kotest-html-reporter", version.toString())

    pom {
        name.set("kotest-html-reporter")
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
