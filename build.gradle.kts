plugins {
	`java-library`
	id("io.papermc.paperweight.userdev") version "1.3.3"
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
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")
}

tasks {
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