# Example: Using NoctisUI as a Dependency

This is a minimal example showing how to use NoctisUI in your own Fabric mod.

## Project Structure

```
your-mod/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/yourmod/
│       │       └── YourMod.java
│       └── resources/
│           └── fabric.mod.json
└── ~/.gradle/
    └── gradle.properties  (for GitHub credentials)
```

## 1. build.gradle

```gradle
plugins {
    id 'fabric-loom' version '1.6-SNAPSHOT'
    id 'maven-publish'
}

version = '1.0.0'
group = 'com.example'

repositories {
    mavenCentral()
    
    // Add GitHub Packages repository for NoctisUI
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/AxenoDev/NoctisUI")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    // Minecraft and Fabric
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"
    
    // NoctisUI dependency
    modImplementation "fr.libnaus:noctisui:${project.noctisui_version}"
}

processResources {
    inputs.property "version", project.version

    filesMatching("fabric.mod.json") {
        expand "version": project.version
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType(JavaCompile).configureEach {
    it.options.encoding = "UTF-8"
    it.options.release = 17
}
```

## 2. gradle.properties

```properties
# Minecraft Properties
minecraft_version=1.20.1
yarn_mappings=1.20.1+build.2
loader_version=0.16.14

# Mod Properties
mod_version=1.0.0
maven_group=com.example
archives_base_name=your-mod

# Dependencies
fabric_version=0.92.6+1.20.1
noctisui_version=1.0.0
```

## 3. ~/.gradle/gradle.properties

Create this file in your home directory with your GitHub credentials:

```properties
# GitHub Packages credentials
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

**Important:** Generate a Personal Access Token at https://github.com/settings/tokens with `read:packages` permission.

## 4. settings.gradle

```gradle
pluginManagement {
    repositories {
        maven {
            name = 'Fabric'
            url = 'https://maven.fabricmc.net/'
        }
        gradlePluginPortal()
    }
}
```

## 5. fabric.mod.json

```json
{
  "schemaVersion": 1,
  "id": "yourmod",
  "version": "${version}",
  "name": "Your Mod",
  "description": "Your mod using NoctisUI",
  "authors": [
    "You"
  ],
  "contact": {},
  "license": "MIT",
  "icon": "assets/yourmod/icon.png",
  "environment": "*",
  "entrypoints": {
    "client": [
      "com.example.yourmod.YourMod"
    ]
  },
  "depends": {
    "fabricloader": ">=0.16.14",
    "minecraft": "~1.20.1",
    "java": ">=17",
    "fabric-api": "*",
    "noctisui": ">=1.0.0"
  }
}
```

## 6. Example Code (YourMod.java)

```java
package com.example.yourmod;

import net.fabricmc.api.ClientModInitializer;
import fr.libnaus.noctisui.client.component.Button;
import fr.libnaus.noctisui.client.utils.Color;

public class YourMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Your mod with NoctisUI initialized!");
        
        // Example: Create a button using NoctisUI
        Button button = new Button(
            100, 50,           // x, y position
            120, 30,           // width, height
            "Click Me!",       // label text
            new Color(0, 122, 204),  // background color
            Color.WHITE        // text color
        );
        
        button.setOnClick(b -> {
            System.out.println("NoctisUI button clicked!");
        });
    }
}
```

## Building Your Mod

1. **Set up credentials** in `~/.gradle/gradle.properties`

2. **Build the mod:**
   ```bash
   ./gradlew build
   ```

3. **Find the JAR** in `build/libs/`

## Troubleshooting

### "Could not resolve fr.libnaus:noctisui"

- Make sure you have valid GitHub credentials configured
- Verify the token has `read:packages` permission
- Check that the NoctisUI version exists

### "401 Unauthorized"

- Your GitHub token is invalid or expired
- Token doesn't have `read:packages` scope
- Credentials are not properly set in gradle.properties

### Build errors with Loom

- Make sure you're using compatible versions of Minecraft and Loom
- Verify all version numbers in `gradle.properties` match

## Next Steps

- Read the [NoctisUI Documentation](https://github.com/AxenoDev/NoctisUI/docs)
- Check out [example components](https://github.com/AxenoDev/NoctisUI/docs/examples)
- Join the community and ask questions
