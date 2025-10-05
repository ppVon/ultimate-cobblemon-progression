plugins {
    id("java")
    id("dev.architectury.loom") version("1.7-SNAPSHOT")
    id("architectury-plugin") version("3.4-SNAPSHOT")
    kotlin("jvm") version "1.9.23"
}

group = "org.ppvon"
version = "1.0.0-SNAPSHOT"

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("mixins.${project.name}.refmap.json")
    }
}

repositories {
    mavenCentral()
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.ladysnake.org/releases")
    maven("https://api.modrinth.com/maven") // Moonlight
    maven("https://maven.shedaniel.me")
    maven ("https://maven.terraformersmc.com/")
}

dependencies {
    minecraft("net.minecraft:minecraft:1.21.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.5")

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:0.104.0+1.21.1")
    modImplementation(fabricApi.module("fabric-command-api-v2", "0.104.0+1.21.1"))
    modImplementation(fabricApi.module("fabric-resource-loader-v0", "0.104.0+1.21.1"))
    modImplementation(fabricApi.module("fabric-lifecycle-events-v1", "0.104.0+1.21.1"))
    modImplementation(fabricApi.module("fabric-networking-api-v1", "0.104.0+1.21.1"))
    modImplementation(fabricApi.module("fabric-entity-events-v1", "0.104.0+1.21.1"))
    modImplementation(fabricApi.module("fabric-events-interaction-v0", "0.104.0+1.21.1"))

    modRuntimeOnly("me.shedaniel.cloth:cloth-config-fabric:15.0.130")
    modCompileOnly("me.shedaniel.cloth:cloth-config-fabric:15.0.130") // optional if you don't directly compile against it

    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.3+kotlin.2.0.21")
    modImplementation("com.cobblemon:fabric:1.6.0+1.21.1-SNAPSHOT")

    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:6.1.2")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:6.1.2")

    modImplementation("maven.modrinth:moonlight:1.21-2.23.11-fabric")
    modCompileOnly("com.terraformersmc:modmenu:11.0.3")
    modRuntimeOnly("com.terraformersmc:modmenu:11.0.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(project.properties)
    }
}

tasks.named<JavaExec>("runClient") {
    args("--username", "UCP_Tester")
    //30815191-a90b-433a-aa64-e3e819bcc4be

    //args("--username", "UCP_Tester-2")
}