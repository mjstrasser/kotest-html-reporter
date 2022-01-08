import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "mjs"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotestVersion = "5.0.3"
val kloggingVersion = "0.4.1"

dependencies {
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    implementation("io.kotest:kotest-extensions-junitxml:$kotestVersion")
    implementation("io.klogging:klogging-jvm:$kloggingVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}