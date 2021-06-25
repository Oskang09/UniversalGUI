import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "me.oska"
version = "3.0.6"

val kotlinVersion = "1.5.10"

plugins {
    java
    maven
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=compatibility")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = URI.create("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
    maven { url = URI.create("https://jitpack.io") }
    maven { url = URI.create("http://repo.citizensnpcs.co") }
    maven { url = URI.create("http://repo.extendedclip.com/content/repositories/placeholderapi") }
    maven { url = URI.create("http://nexus.okkero.com/repository/maven-releases/") }
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8", kotlinVersion))
    testImplementation(kotlin("test"))

    compileOnly("org.spigotmc:spigot-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.PlaceholderAPI:PlaceholderAPI:2.10.9")
    compileOnly("net.citizensnpcs:citizensapi:2.0.26-SNAPSHOT")

    implementation("com.github.JavaFactoryDev:LightningStorage:3.0.7")
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    manifest {
        attributes(mapOf("Main-Class" to "$group/UniversalGUI"))
    }
}

val shadowJar = (tasks["shadowJar"] as ShadowJar).apply {
    exclude("kotlin/**")
}

val deployPath: String by project
val deployPlugin by tasks.registering(Copy::class) {
    dependsOn(shadowJar)

    System.getenv("PLUGIN_DEPLOY_PATH")?.let {
        from(shadowJar)
        into(it)
    }
}

val test = (tasks["test"] as Task).apply {

}

val build = (tasks["build"] as Task).apply {
    arrayOf(
        sourcesJar
        , shadowJar
        , deployPlugin
    ).forEach { dependsOn(it) }
}