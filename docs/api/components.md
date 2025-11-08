# Components API

NoctisUI provides a set of UI components that you can use to build your interfaces. All components implement the `UIComponent` interface.

## Core Components

### Button

An interactive button component with hover effects, animations, and click handling.

**Constructor:**
```java
Button(float x, float y, float width, float height, String label, Color backgroundColor, Color labelColor)
```

**Basic Example:**
```java
Button button = new Button(100, 50, 120, 30, "Click Me", 
    new Color(0, 122, 204), Color.WHITE);
button.setOnClick(b -> System.out.println("Clicked!"));
```

**Customization Methods:**

| Method | Description |
|--------|-------------|
| `setOnClick(Consumer<Button>)` | Set the click handler |
| `hover(long duration, Color bgColor, Color textColor)` | Enable hover animation |
| `setOutline(Color color, float width)` | Add an outline border |
| `setRadius(int radius)` | Set corner radius for rounded corners |
| `setFont(FontAtlas font)` | Set custom font |
| `setFontSize(int size)` | Set font size |
| `setShadow(boolean shadow)` | Enable/disable text shadow |

**Advanced Example:**
```java
Button fancyButton = new Button(100, 100, 150, 40, "Fancy Button",
    new Color(41, 128, 185), Color.WHITE);

// Add hover effect with 200ms animation
fancyButton.hover(200, new Color(52, 152, 219), new Color(236, 240, 241));

// Add outline
fancyButton.setOutline(Color.BLACK, 2);

// Set rounded corners
fancyButton.setRadius(8);

// Use custom font
fancyButton.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());
fancyButton.setFontSize(14);

// Enable text shadow
fancyButton.setShadow(true);

// Set click handler
fancyButton.setOnClick(b -> {
    System.out.println("Fancy button clicked!");
});
```

### TextComponent

A component for rendering text with custom fonts, sizes, and colors.

**Constructors:**
```java
// With default font
TextComponent(float x, float y, String text, float fontSize, Color color)

// With custom font
TextComponent(float x, float y, String text, float fontSize, Color color, FontAtlas font)
```

**Example:**
```java
// Simple text
TextComponent text = new TextComponent(50, 50, "Hello, World!", 16, Color.WHITE);

// Custom font
FontAtlas customFont = NoctisUIClient.getInstance().getFonts().getPoppins();
TextComponent customText = new TextComponent(50, 100, "Custom Font", 18, 
    new Color(255, 193, 7), customFont);
```

**Methods:**

| Method | Description |
|--------|-------------|
| `setText(String text)` | Update the text content |
| `setFontSize(float size)` | Change font size |
| `setColor(Color color)` | Change text color |
| `setFont(FontAtlas font)` | Change font |

### ImageComponent

Display images from resource locations in your UI.

**Constructor:**
```java
ImageComponent(float x, float y, float width, float height, Identifier texture)
```

**Example:**
```java
Identifier logoTexture = new Identifier("mymod", "textures/ui/logo.png");
ImageComponent logo = new ImageComponent(50, 50, 200, 100, logoTexture);
```

### DivComponent

A versatile container component that can hold multiple child components. Acts as a layout container with its own background, outline, and click handling.

**Constructor:**
```java
DivComponent(float x, float y, float width, float height)
```

**Basic Example:**
```java
DivComponent container = new DivComponent(10, 10, 300, 200);
container.setBackgroundColor(new Color(0, 0, 0, 150)); // Semi-transparent black
container.setCornerRadius(10);

// Add children
container.addChild(new TextComponent(20, 20, "Title", 18, Color.WHITE));
container.addChild(new Button(20, 60, 100, 30, "Action", 
    new Color(52, 152, 219), Color.WHITE));
```

**Methods:**

| Method | Description |
|--------|-------------|
| `addChild(UIBaseComponent child)` | Add a child component |
| `removeChild(UIBaseComponent child)` | Remove a child component |
| `removeIf(Predicate<UIBaseComponent>)` | Remove children matching condition |
| `clearChildren()` | Remove all children |
| `setBackgroundColor(Color color)` | Set background color |
| `setCornerRadius(float radius)` | Set corner radius |
| `setOutline(Color color, float width)` | Set outline |
| `setOnClick(Consumer<DivComponent>)` | Set click handler |
| `setCustomRenderer(Runnable renderer)` | Set custom render hook |

**Advanced Example:**
```java
DivComponent panel = new DivComponent(50, 50, 400, 300);
panel.setBackgroundColor(new Color(44, 62, 80));
panel.setCornerRadius(12);
panel.setOutline(new Color(52, 73, 94), 2);

// Add header
TextComponent header = new TextComponent(20, 15, "Settings Panel", 20, Color.WHITE);
panel.addChild(header);

// Add buttons
Button saveBtn = new Button(20, 250, 150, 35, "Save", 
    new Color(46, 204, 113), Color.WHITE);
Button cancelBtn = new Button(230, 250, 150, 35, "Cancel", 
    new Color(231, 76, 60), Color.WHITE);
    
panel.addChild(saveBtn);
panel.addChild(cancelBtn);

// Click handler for the panel itself
panel.setOnClick(div -> System.out.println("Panel clicked"));
```

### TextInput

A text input field component for user text entry.

**Constructor:**
```java
TextInput(float x, float y, float width, float height)
```

**Example:**
```java
TextInput input = new TextInput(50, 50, 200, 30);
input.setPlaceholder("Enter your name...");
input.setMaxLength(50);

// Get the entered text
String value = input.getValue();
```

## Available Fonts

NoctisUI comes with several pre-loaded fonts accessible via:

```java
Fonts fonts = NoctisUIClient.getInstance().getFonts();

// Available fonts:
FontAtlas interMedium = fonts.getInterMedium();
FontAtlas interBold = fonts.getInterBold();
FontAtlas poppins = fonts.getPoppins();
// ... and more
```

## Color Utilities

The `Color` class provides several utilities:

```java
// Create colors
Color red = new Color(255, 0, 0);          // RGB
Color translucentBlue = new Color(0, 0, 255, 128);  // RGBA
Color white = Color.WHITE;                  // Predefined colors

// Color operations
Color interpolated = Color.interpolateColor(color1, color2, 0.5f);
int rgb = color.getRGB();
int rgba = color.getRGBA();
```

## Rendering

All components should be rendered in your screen's `render()` method:

```java
@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    super.render(context, mouseX, mouseY, delta);
    
    // Render your components
    myButton.render(context, mouseX, mouseY, delta);
    myDiv.render(context, mouseX, mouseY, delta);
}
```

## Event Handling

Handle mouse clicks in your screen's `mouseClicked()` method:

```java
@Override
public boolean mouseClicked(double mouseX, double mouseY, int button) {
    // Let components handle clicks
    if (myButton.mouseClicked(mouseX, mouseY, button)) {
        return true;
    }
    if (myDiv.mouseClicked(mouseX, mouseY, button)) {
        return true;
    }
    
    return super.mouseClicked(mouseX, mouseY, button);
}
```

## Next Steps

- Check out [Examples](/examples/basic-button) for practical use cases
- Learn about [Styling](/guides/styling) to customize your components
