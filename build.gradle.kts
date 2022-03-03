val minecraft_version="1.18.1"
val spigot_version="1.18.1-R0.1-SNAPSHOT"

plugins {
	`java-library`
	id("io.github.rancraftplayz.remapper") version "1.0.0"
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
	compileOnly("org.spigotmc:spigot:${spigot_version}:remapped-mojang")
	remapLib("org.spigotmc:spigot:${spigot_version}:remapped-mojang")
}

spigot {
	version = minecraft_version
}

tasks.named("jar") { finalizedBy("remapJar") } 