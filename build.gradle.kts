import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    kotlin ("jvm") version "1.4.21"
    kotlin("kapt") version "1.4.0"
}

group = "io.challange"
version = "1.0-SNAPSHOT"
var arrowVersion = "0.11.0"
val junitJupiterVersion = "5.7.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    //Arrow
    implementation ("io.arrow-kt:arrow-core:$arrowVersion")
    implementation ("io.arrow-kt:arrow-syntax:$arrowVersion")
    // Logging
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:2.0.3")
    kapt    ("io.arrow-kt:arrow-meta:$arrowVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    implementation ("com.google.guava:guava:21.0")
    testImplementation("io.mockk:mockk:1.10.2")
    testImplementation("org.awaitility:awaitility:4.0.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED,
            SKIPPED,
            FAILED
        )
    }
}