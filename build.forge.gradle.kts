plugins {
    id("net.neoforged.moddev.legacyforge")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val loader = "forge"

fun prop(name: String) = project.property(name) as String

version = prop("mod.version")
group = prop("mod.group")
base.archivesName = "${prop("mod.id")}-$loader-$minecraft"

sourceSets.main { resources.srcDir(rootProject.file("src/main/overlays/$loader")) }

val refmap = "${prop("mod.id")}.refmap.json"
val mixinConfigs = listOf("${prop("mod.id")}.mixins.json", "${prop("mod.id")}.forge.mixins.json")

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" }
    maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
    maven("https://maven.quiltmc.org/repository/release/") { name = "Quilt Maven" }
    maven("https://maven.cassian.cc/") { name = "Cassian's Maven" }
}

mixin {
    add(sourceSets["main"], refmap)
    mixinConfigs.forEach(::config)
}

legacyForge {
    version = "$minecraft-${prop("deps.forge")}"
    validateAccessTransformers = true

    parchment {
        minecraftVersion = minecraft
        mappingsVersion = prop("deps.parchment")
    }

    runs {
        register("client") {
            client()
        }
        register("server") {
            server()
        }
    }

    mods {
        register(prop("mod.id")) {
            sourceSet(sourceSets["main"])
        }
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.jetbrains:annotations:26.0.2-1")

    // EMI
    modImplementation("dev.emi:emi-forge:${prop("deps.emi")}")

    // YACL
    modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}-forge")
}

val javaVer = prop("deps.java_version").toInt()
java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVer)
    withSourcesJar()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release = javaVer
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
            "group" to project.group,
            "minecraft_version" to minecraft,
            "minecraft_version_range" to prop("mod.mc_dep_forgelike"),
            "forge_version" to prop("deps.forge"),
            "forge_loader_version_range" to prop("deps.forge_loader_version_range"),
            "yacl_version" to prop("deps.yacl"),
            "mod_name" to prop("mod.name"),
            "mod_author" to prop("mod.author"),
            "mod_id" to prop("mod.id"),
            "license" to prop("mod.license"),
            "description" to prop("mod.description"),
            "credits" to prop("mod.credits"),
            "java_version" to javaVer,
            "pack_format" to prop("mod.pack_format"),
        )
        inputs.properties(props)

        filesMatching(listOf("pack.mcmeta", "META-INF/mods.toml", "*.mixins.json")) {
            expand(props)
        }

        // Forge runs on SRG names, so the mixin configs need to point at the generated refmap
        filesMatching(mixinConfigs) {
            filter { line -> line.replace("\"package\":", "\"refmap\": \"$refmap\",\n  \"package\":") }
        }

        // Tag folders were singularised in 1.21
        if (stonecutter.eval(minecraft, "<1.21")) {
            filesMatching("data/*/tags/block/**") {
                path = path.replace("/tags/block/", "/tags/blocks/")
            }
        }
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    jar {
        finalizedBy("reobfJar")

        from(rootProject.file("LICENSE")) {
            rename { "${it}_${prop("mod.name")}" }
        }

        manifest.attributes(
            "Specification-Title" to prop("mod.name"),
            "Specification-Vendor" to prop("mod.author"),
            "Specification-Version" to archiveVersion,
            "Implementation-Title" to loader,
            "Implementation-Version" to archiveVersion,
            "Implementation-Vendor" to prop("mod.author"),
            "Built-On-Minecraft" to minecraft,
            "MixinConfigs" to mixinConfigs.joinToString(","),
        )
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(named<Jar>("reobfJar").map { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.version}"))
        dependsOn("build")
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
    repositories {
        System.getenv("local_maven_url")?.let { maven(it) }
    }
}

publishMods {
    file = tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    type = STABLE
    version = "${project.version}-$minecraft-$loader"
    displayName = "${prop("mod.name")} Forge $minecraft - ${project.version}"
    modLoaders.add(loader)

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        projectId = prop("publish.curseforge")
        minecraftVersions.add(minecraft)
        client = true
        server = true

        optional("yacl")
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = prop("publish.modrinth")
        minecraftVersions.add(minecraft)

        optional("yacl")
    }
}
