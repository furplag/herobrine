plugins {
	`java-library`
	id("io.papermc.paperweight.userdev") version "1.3.3"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    // libs folder (TODO: transfer away from this!)
    flatDir {
        dirs = setOf(file("libs"))
    }
}

dependencies {
	implementation(":Factions")
	implementation(":GriefPrevention")
	implementation(":MassiveCore")
    implementation(":PreciousStones")
    implementation(":RedProtect")
    implementation(":Residence")
    implementation(":Towny")
    implementation(":WorldEdit")
    implementation(":WorldGuard")
    shadow("net.kyori:adventure-api:4.9.3")
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")
}

tasks {
	shadowJar {
        configurations = listOf(project.configurations.shadow.get())
    }

	assemble {
		dependsOn(reobfJar)
	}
	
	reobfJar {
		outputJar.set(layout.buildDirectory.file("libs/Herobrine 2.jar"))
	}
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}