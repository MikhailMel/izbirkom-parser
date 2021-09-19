plugins {
    kotlin("jvm") version "1.5.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("net.sourceforge.tess4j:tess4j:4.5.5")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.5")
}