plugins {
    kotlin("jvm") version "1.8.0"
}

group = "kr.hqservice.menu"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.MinseoServer", "MS-Core", "1.0.18")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("${rootProject.name}-${project.version}.jar")
    destinationDirectory.set(File("D:\\서버\\1.19.3 - 개발\\plugins"))
}