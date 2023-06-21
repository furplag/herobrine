val mc_version="1.20"

plugins {
	`java-library`
	id("io.github.patrick.remapper") version "1.4.0"
}

repositories {
  mavenCentral()
  maven("https://maven.elmakers.com/repository/")
  maven("https://maven.enginehub.org/repo/") /* worldedit, worldguard */
}

dependencies {
  compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.14-SNAPSHOT")
  compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.1.0-SNAPSHOT")
  compileOnly("org.spigotmc:spigot:${mc_version}-R0.1-SNAPSHOT:remapped-mojang")

  compileOnly("org.projectlombok:lombok:1.18.28")
  annotationProcessor("org.projectlombok:lombok:1.18.28")
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
