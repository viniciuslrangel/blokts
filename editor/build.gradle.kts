import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.*

val packageName = "io.github.viniciuslrangel.blokts"
val pluginVersion = "0.1"
val kotlinVersion = "1.3.11"

plugins {
    application
    kotlin("jvm") version "1.3.11"
    kotlin("kapt") version "1.3.11"
}

group = packageName
version = pluginVersion

application {
    mainClassName = "io.github.viniciuslrangel.blokts.EditorTester"
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))

    implementation(project(":generator"))
    kapt(project(":generator"))
}

kapt {
    correctErrorTypes = true
}
