val mc_version="1.18.1"

plugins {
	`java-library`
	id("io.github.patrick.remapper") version "1.2.0"
}

repositories {
	mavenCentral()
	maven("https://repo.spongepowered.org/maven")
	maven("https://maven.elmakers.com/repository/")
	mavenLocal()

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
	compileOnly("org.spigotmc:spigot:${mc_version}-R0.1-SNAPSHOT:remapped-mojang")
}

tasks {
	remap {
		version.set("${mc_version}")
	}
}

tasks.named("jar") { finalizedBy("remap") } 