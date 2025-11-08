# Getting Started

Welcome to NoctisUI! This guide will help you get started with creating beautiful UIs for your Minecraft mods.

## What is NoctisUI?

NoctisUI is a powerful UI library for Minecraft (Fabric) that provides developers with a simple yet flexible API to create custom user interfaces. Whether you're building menus, HUDs, or interactive screens, NoctisUI has the tools you need.

## Requirements

- **Minecraft**: 1.20.1
- **Fabric Loader**: 0.16.14 or higher
- **Fabric API**: 0.92.6+1.20.1 or higher
- **Java**: 17 or higher

## Installation

### For Mod Developers

1. Add NoctisUI as a dependency in your `build.gradle`:

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

dependencies {
    // Add NoctisUI as a dependency
    modImplementation "fr.libnaus:noctisui:1.0.0"
}
```

2. Add NoctisUI to your `fabric.mod.json` dependencies:

```json
{
  "depends": {
    "noctisui": ">=1.0.0"
  }
}
```

::: tip Authentication for GitHub Packages
To access GitHub Packages, you need to provide credentials. You can:
- Add them to your `~/.gradle/gradle.properties`:
  ```properties
  gpr.user=YOUR_GITHUB_USERNAME
  gpr.token=YOUR_GITHUB_TOKEN
  ```
- Or set environment variables `GITHUB_ACTOR` and `GITHUB_TOKEN`

You can generate a personal access token at [GitHub Settings > Developer settings > Personal access tokens](https://github.com/settings/tokens) with `read:packages` permission.
:::

### For Players

1. Download the latest version of NoctisUI from the releases page
2. Place the `.jar` file in your Minecraft `mods` folder
3. Make sure you have Fabric Loader and Fabric API installed

## Your First UI Component

Let's create a simple button to get started:

```java
import fr.libnaus.noctisui.client.component.Button;
import fr.libnaus.noctisui.client.utils.Color;

// Create a button at position (100, 50) with size 120x30
Button myButton = new Button(
    100, 50,           // x, y position
    120, 30,           // width, height
    "Click Me!",       // label text
    new Color(0, 122, 204),  // background color (blue)
    Color.WHITE        // text color
);

// Add a click handler
myButton.setOnClick(button -> {
    System.out.println("Button was clicked!");
});

// Render the button in your screen's render method
myButton.render(context, mouseX, mouseY, delta);
```

## Basic Concepts

### Components

NoctisUI is built around a component-based architecture. Everything you see in your UI is a component:

- **Button**: Interactive button with hover effects and click handlers
- **TextComponent**: Simple text display with custom fonts and colors
- **ImageComponent**: Display images from resources
- **DivComponent**: Container for grouping other components
- **TextInput**: Text input field for user input

### Rendering

Components implement the `UIComponent` interface which requires a `render()` method:

```java
void render(DrawContext context, double mouseX, double mouseY, float delta);
```

You typically call this method in your screen's `render()` method.

### Event Handling

Components can handle mouse events:

```java
boolean mouseClicked(double mouseX, double mouseY, int button);
```

## Next Steps

Now that you understand the basics, explore:

- [Components API](/api/components) - Learn about all available components
- [Examples](/examples/basic-button) - See practical examples
- [Styling Guide](/guides/styling) - Learn how to customize your components

## Need Help?

- Check out the [Examples](/examples/basic-button) for common use cases
- Read the [API Reference](/api/components) for detailed component documentation
- Join our community on [GitHub](https://github.com/AxenoDev/NoctisUI)
