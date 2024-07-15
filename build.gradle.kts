plugins {
    id("java")
}

group = "com.alazeprt.serverstore"
version = "1.0"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
}