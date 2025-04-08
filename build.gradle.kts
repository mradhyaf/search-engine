buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("io.ratpack:ratpack-gradle:2.0.0-rc-1")
        classpath("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.0.0-beta12")
    }
}

plugins {
    id("io.ratpack.ratpack-java") version "2.0.0-rc-1"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.xerial:sqlite-jdbc:3.49.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("id.mradhyaf.searchengine.server.Main")
}

tasks.test {
    useJUnitPlatform()
}