buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.ratpack:ratpack-gradle:2.0.0-rc-1")
    }
}

plugins {
    id("java")
    id("io.ratpack.ratpack-java") version "2.0.0-rc-1" // https://plugins.gradle.org/plugin/io.ratpack.ratpack-java
    id("idea")
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