#!/bin/bash
# Script to test Maven publishing locally

echo "==========================================="
echo "NoctisUI Maven Publishing Test"
echo "==========================================="
echo ""

# Make gradlew executable
chmod +x gradlew

# Test 1: Publish to local Maven repository
echo "Test 1: Publishing to local Maven repository..."
echo "-------------------------------------------"
./gradlew publishToMavenLocal

if [ $? -eq 0 ]; then
    echo "✓ Successfully published to local Maven repository"
    echo ""
    
    # Check if the artifact was created
    MAVEN_LOCAL=~/.m2/repository
    ARTIFACT_PATH="$MAVEN_LOCAL/fr/libnaus/noctisui"
    
    if [ -d "$ARTIFACT_PATH" ]; then
        echo "✓ Artifact found at: $ARTIFACT_PATH"
        echo ""
        echo "Published versions:"
        ls -la "$ARTIFACT_PATH"
        echo ""
    else
        echo "✗ Artifact not found at expected location: $ARTIFACT_PATH"
    fi
else
    echo "✗ Failed to publish to local Maven repository"
    exit 1
fi

# Test 2: Show generated POM
echo ""
echo "Test 2: Checking generated POM..."
echo "-------------------------------------------"
POM_FILE=$(find build/publications/mavenJava -name "*.pom" 2>/dev/null | head -1)

if [ -n "$POM_FILE" ] && [ -f "$POM_FILE" ]; then
    echo "✓ POM file found: $POM_FILE"
    echo ""
    echo "POM content:"
    cat "$POM_FILE"
    echo ""
else
    echo "✗ POM file not found"
fi

# Test 3: List published artifacts
echo ""
echo "Test 3: Listing published artifacts..."
echo "-------------------------------------------"
if [ -d "build/publications/mavenJava" ]; then
    echo "✓ Publications directory exists"
    echo ""
    echo "Generated files:"
    find build/publications/mavenJava -type f
    echo ""
else
    echo "✗ Publications directory not found"
fi

echo ""
echo "==========================================="
echo "Testing complete!"
echo "==========================================="
echo ""
echo "To use this artifact in another project, add to build.gradle:"
echo ""
echo "repositories {"
echo "    mavenLocal()"
echo "}"
echo ""
echo "dependencies {"
echo "    modImplementation 'fr.libnaus:noctisui:<version>'"
echo "}"
echo ""
