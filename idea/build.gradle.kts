import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.*

val packageName = "io.github.viniciuslrangel.blokts"
val pluginVersion = "0.1"
val kotlinVersion = "1.3.11"

plugins {
    java
    id("org.jetbrains.intellij") version "0.3.12"
    kotlin("jvm") version "1.3.11"
}

group = packageName
version = pluginVersion

repositories {
    mavenCentral()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<PatchPluginXmlTask> {
    changeNotes(file("info/change-notes.html").readText())
    pluginDescription(file("info/description.html").readText())
    version(pluginVersion)
    pluginId(packageName)
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(project(":editor"))
}

intellij {
    version = "2018.3"
    setPlugins("kotlin")
    sandboxDirectory = "${project.buildDir}/idea-sandbox"
}
