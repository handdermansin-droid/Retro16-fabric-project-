# Retro16 (Fabric, Minecraft 1.14.4)

Requirements: JDK 8, internet access for the first build.

## Build
1. Install Gradle 5.6.4 (or any Gradle 5.x), then in this folder run once:
       gradle wrapper --gradle-version 5.6.4
2. Build:
       ./gradlew build        (Windows: gradlew.bat build)
3. The mod is build/libs/retro16-1.0.0.jar (NOT the -sources jar).

## Test
       ./gradlew runClient

## Install into a real game
Use the official Minecraft launcher + the Fabric installer (https://fabricmc.net/use/)
for 1.14.4, then drop retro16-1.0.0.jar into the .minecraft/mods folder.

## If it crashes at launch with "injection point not found"
A Yarn mapping name differs from what the code expects. Run ./gradlew genSources,
open the named Minecraft class, and fix the method/field name in the matching mixin.
