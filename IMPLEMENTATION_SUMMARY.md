# Summary: Maven Publishing Configuration for NoctisUI

## Problem Statement (French)
> Fait moi un code pour mettre le build de noctisui en dépendence, pour le publier sur maven

**Translation:** Make me code to put the noctisui build as a dependency, to publish it on maven.

## Solution Overview

This implementation configures NoctisUI to be publishable on Maven (specifically GitHub Packages) and usable as a dependency in other Fabric mods.

## What Was Done

### 1. Build Configuration (`build.gradle`)

Enhanced the existing Maven publishing configuration with:

```gradle
publishing {
    publications {
        create("mavenJava", MavenPublication) {
            groupId = project.maven_group          // fr.libnaus
            artifactId = project.archives_base_name // noctisui
            version = project.version               // Dynamic from git tag
            
            from components.java                    // Includes JAR and sources
            
            pom {
                // Complete metadata
                name = 'NoctisUI'
                description = 'A Minecraft UI library...'
                url = 'https://github.com/AxenoDev/NoctisUI'
                licenses { ... }
                developers { ... }
                scm { ... }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/AxenoDev/NoctisUI")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        mavenLocal()  // For local testing
    }
}
```

**Key Features:**
- Complete POM metadata (name, description, license, developers, SCM)
- GitHub Packages as primary repository
- Local Maven repository for testing
- Automatic inclusion of sources JAR
- Uses existing version from git tags

### 2. GitHub Actions Workflow (`.github/workflows/release-published.yml`)

Added automatic publishing job:

```yaml
jobs:
  publish-maven:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write
    steps:
      - Checkout code
      - Set up JDK 21
      - Run ./gradlew publish
  
  notify-discord:
    needs: publish-maven  # Runs after successful publish
```

**Features:**
- Triggers on release publication
- Publishes to GitHub Packages automatically
- Uses proper permissions
- Discord notification only after successful publish

### 3. Documentation

#### MAVEN_PUBLISHING.md
Complete guide covering:
- How NoctisUI is published
- How to use as dependency
- Authentication setup
- Troubleshooting
- Complete examples

#### docs/getting-started.md
Updated with:
- GitHub Packages repository configuration
- Dependency declaration
- Authentication instructions
- gradle.properties setup

#### README.MD
Added:
- Quick start section for using as dependency
- Installation snippet
- Link to detailed documentation

#### docs/examples/dependency-usage.md
Full working example including:
- Complete project structure
- All configuration files
- Example code
- Build instructions
- Troubleshooting

### 4. Testing Utilities

#### test-publish.sh
Bash script that:
- Publishes to local Maven repository
- Verifies artifact creation
- Shows generated POM
- Lists all published artifacts
- Provides usage instructions

## How to Use NoctisUI as a Dependency

### Quick Start

1. **Add repository** to `build.gradle`:
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

2. **Add dependency**:
```gradle
dependencies {
    modImplementation "fr.libnaus:noctisui:1.0.0"
}
```

3. **Configure credentials** in `~/.gradle/gradle.properties`:
```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

4. **Update fabric.mod.json**:
```json
{
  "depends": {
    "noctisui": ">=1.0.0"
  }
}
```

## Publishing Process

### Automatic (Recommended)
1. Create a release on GitHub
2. Workflow automatically publishes to GitHub Packages
3. Artifact is immediately available for consumption

### Manual
```bash
# Publish to GitHub Packages
GITHUB_TOKEN=your_token GITHUB_ACTOR=your_username ./gradlew publish

# Or test locally
./gradlew publishToMavenLocal
```

### Test Local Publishing
```bash
chmod +x test-publish.sh
./test-publish.sh
```

## Artifact Coordinates

- **Group ID**: `fr.libnaus`
- **Artifact ID**: `noctisui`
- **Version**: Follows git tags (e.g., `1.0.0`, `1.1.0-SNAPSHOT-abc1234`)

## Files Modified/Created

### Modified
1. `build.gradle` - Enhanced publishing configuration
2. `.github/workflows/release-published.yml` - Added publish job
3. `settings.gradle` - Added dependency resolution
4. `docs/getting-started.md` - Updated installation
5. `README.MD` - Added dependency usage section

### Created
1. `MAVEN_PUBLISHING.md` - Complete publishing guide
2. `docs/examples/dependency-usage.md` - Full example project
3. `test-publish.sh` - Local testing script

## Benefits

✅ **For NoctisUI Maintainers:**
- Automatic publishing on release
- No manual Maven configuration needed
- Standard Maven coordinates
- Proper versioning from git tags

✅ **For NoctisUI Users:**
- Easy dependency integration
- Standard Gradle/Maven syntax
- Sources included for IDE support
- Comprehensive documentation
- Working examples

## Security

- CodeQL security scan: **0 issues found**
- Credentials stored in environment variables (not in code)
- GitHub token required for publishing (proper permission control)
- Read-only tokens sufficient for consumption

## Next Steps

1. ✅ Configuration complete
2. ⏳ Create first release to test automatic publishing
3. ⏳ Verify package appears in GitHub Packages
4. ⏳ Test consumption in a sample mod project
5. ⏳ Update version numbers as needed

## Notes

- GitHub Packages requires authentication even for public packages
- Users need a GitHub token with `read:packages` permission
- The build uses Fabric Loom 1.11-SNAPSHOT (as per original config)
- Java 21 is required for building (as per GitHub Actions workflow)
- Local testing can be done without GitHub credentials using `publishToMavenLocal`

## Support

For questions or issues:
- Check [MAVEN_PUBLISHING.md](MAVEN_PUBLISHING.md) for detailed info
- See [docs/examples/dependency-usage.md](docs/examples/dependency-usage.md) for complete example
- Open an issue on [GitHub](https://github.com/AxenoDev/NoctisUI/issues)
