# Maven Publishing for NoctisUI

This document explains how NoctisUI is published to Maven and how to use it as a dependency in your Fabric mod.

## Publishing

NoctisUI is automatically published to GitHub Packages when a new release is created. The publishing process:

1. Builds the project with the release version
2. Generates the JAR file with sources
3. Publishes to GitHub Packages at `https://maven.pkg.github.com/AxenoDev/NoctisUI`

### Manual Publishing

To publish manually:

```bash
# Publish to GitHub Packages
GITHUB_TOKEN=your_token GITHUB_ACTOR=your_username ./gradlew publish

# Or publish to local Maven repository for testing
./gradlew publishToMavenLocal
```

## Using NoctisUI as a Dependency

### 1. Add the Repository

Add the GitHub Packages repository to your `build.gradle`:

```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/AxenoDev/NoctisUI")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

### 2. Add the Dependency

Add NoctisUI to your dependencies:

```gradle
dependencies {
    modImplementation "fr.libnaus:noctisui:${project.noctisui_version}"
}
```

### 3. Configure Credentials

You need GitHub credentials to access GitHub Packages. Add to `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

Generate a personal access token at https://github.com/settings/tokens with `read:packages` permission.

### 4. Update fabric.mod.json

Add NoctisUI to your mod's dependencies:

```json
{
  "depends": {
    "noctisui": ">=1.0.0"
  }
}
```

## Artifact Information

- **Group ID**: `fr.libnaus`
- **Artifact ID**: `noctisui`
- **Version**: Follows semantic versioning (e.g., `1.0.0`)

## Available Artifacts

Each release includes:
- Main JAR: `noctisui-{version}.jar`
- Sources JAR: `noctisui-{version}-sources.jar`

## Example build.gradle

Here's a complete example of how to include NoctisUI in your Fabric mod:

```gradle
plugins {
    id 'fabric-loom' version '1.6-SNAPSHOT'
    id 'maven-publish'
}

repositories {
    mavenCentral()
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
    minecraft "com.mojang:minecraft:1.20.1"
    mappings "net.fabricmc:yarn:1.20.1+build.2:v2"
    modImplementation "net.fabricmc:fabric-loader:0.16.14"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.92.6+1.20.1"
    
    // NoctisUI
    modImplementation "fr.libnaus:noctisui:1.0.0"
}
```

## Troubleshooting

### Authentication Issues

If you encounter authentication issues:

1. Make sure you have created a personal access token with `read:packages` permission
2. Verify the token is not expired
3. Check that your credentials are correctly set in `gradle.properties` or environment variables

### Package Not Found

If Gradle cannot find the package:

1. Verify the package has been published (check GitHub Packages page)
2. Ensure you're using the correct group ID and artifact ID
3. Check that the version you're requesting exists

### Permission Denied

GitHub Packages requires authentication even for public packages. Make sure you have:

1. A valid GitHub personal access token
2. The `read:packages` scope enabled on the token
3. Correctly configured credentials in your Gradle configuration
