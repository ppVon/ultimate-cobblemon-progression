plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    common("neoforge", "fabric")
}

loom {
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("ucp.refmap.json")
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://repo.spongepowered.org/repository/maven-public")
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}") { isTransitive = false }

    //testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")

    compileOnly("org.spongepowered:mixin:0.8.5")
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")

    modImplementation("dev.architectury:architectury:${property("architectury_api_version")}")
}

/*
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
 */
