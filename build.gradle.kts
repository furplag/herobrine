val mc_version="1.19.3"

plugins {
	`java-library`
	id("io.github.patrick.remapper") version "1.3.0"
}

repositories {
	mavenCentral()
	maven("https://repo.spongepowered.org/maven")
	maven("https://maven.elmakers.com/repository/")
	maven("https://maven.enginehub.org/repo/") /* worldedit, worldguard */
	maven("https://jitpack.io/") /* GriefPrevention, Towny */
	mavenLocal()

	// libs folder (TODO: transfer away from this!)
	flatDir {
		dirs = setOf(file("libs"))
	}
}

dependencies {
	compileOnly(":Factions")
	compileOnly("com.github.TechFortress:GriefPrevention:16.18")
	compileOnly(":MassiveCore")
	compileOnly(":PreciousStones")
	compileOnly(":RedProtect")
	compileOnly(":Residence")
	compileOnly("com.github.townyadvanced:towny:0.98.4.15")
	compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.14-SNAPSHOT")
	compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.1.0-SNAPSHOT")
	compileOnly("org.spigotmc:spigot:${mc_version}-R0.1-SNAPSHOT:remapped-mojang")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
	remap {
		version.set("${mc_version}")
	}
}

tasks.named("jar") { finalizedBy("remap") }

version="2.5.0-SNAPSHOT-20221226"
