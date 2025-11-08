# Styling Guide

Learn how to customize the appearance of NoctisUI components to match your mod's design.

## Colors

NoctisUI uses the `Color` class for all color operations.

### Creating Colors

```java
// RGB (opaque)
Color red = new Color(255, 0, 0);
Color green = new Color(0, 255, 0);
Color blue = new Color(0, 0, 255);

// RGBA (with transparency)
Color translucentBlack = new Color(0, 0, 0, 128);  // 50% transparent
Color semiTransparent = new Color(255, 255, 255, 200);

// Using predefined colors
Color white = Color.WHITE;
Color black = Color.BLACK;
// ... and more predefined colors
```

### Color Operations

```java
// Interpolate between two colors
Color start = new Color(255, 0, 0);      // Red
Color end = new Color(0, 0, 255);        // Blue
Color middle = Color.interpolateColor(start, end, 0.5f); // Purple

// Get color components
int rgb = color.getRGB();
int rgba = color.getRGBA();
```

### Common Color Palettes

Here are some ready-to-use color schemes:

**Material Design Colors:**
```java
// Primary colors
Color materialRed = new Color(244, 67, 54);
Color materialPink = new Color(233, 30, 99);
Color materialPurple = new Color(156, 39, 176);
Color materialBlue = new Color(33, 150, 243);
Color materialGreen = new Color(76, 175, 80);

// Grays
Color gray50 = new Color(250, 250, 250);
Color gray100 = new Color(245, 245, 245);
Color gray200 = new Color(238, 238, 238);
Color gray500 = new Color(158, 158, 158);
Color gray900 = new Color(33, 33, 33);
```

**Flat UI Colors:**
```java
Color turquoise = new Color(26, 188, 156);
Color emerald = new Color(46, 204, 113);
Color peterRiver = new Color(52, 152, 219);
Color amethyst = new Color(155, 89, 182);
Color wetAsphalt = new Color(52, 73, 94);
Color sunflower = new Color(241, 196, 15);
Color carrot = new Color(230, 126, 34);
Color alizarin = new Color(231, 76, 60);
Color clouds = new Color(236, 240, 241);
Color concrete = new Color(149, 165, 166);
```

## Fonts

NoctisUI includes several pre-loaded fonts.

### Available Fonts

```java
Fonts fonts = NoctisUIClient.getInstance().getFonts();

FontAtlas interMedium = fonts.getInterMedium();
FontAtlas interBold = fonts.getInterBold();
FontAtlas poppins = fonts.getPoppins();
// Check the Fonts class for all available fonts
```

### Using Custom Fonts

```java
Button button = new Button(100, 50, 120, 30, "Custom Font",
    new Color(52, 152, 219), Color.WHITE);

// Set a different font
button.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());

// Adjust font size
button.setFontSize(16);

// Enable text shadow for better readability
button.setShadow(true);
```

### Font Sizes

Common font sizes for different use cases:

- **Small text**: 10-12px (details, labels)
- **Body text**: 12-14px (normal content)
- **Headers**: 16-20px (section titles)
- **Large headers**: 24-32px (page titles)

## Borders and Outlines

Add outlines to make components stand out:

```java
Button button = new Button(100, 50, 120, 30, "Outlined",
    Color.WHITE, new Color(52, 152, 219));

// Add a 2px blue outline
button.setOutline(new Color(52, 152, 219), 2);

// For containers
DivComponent panel = new DivComponent(50, 50, 300, 200);
panel.setBackgroundColor(Color.WHITE);
panel.setOutline(new Color(189, 195, 199), 1); // Subtle gray border
```

## Rounded Corners

Create modern, rounded designs:

```java
// Buttons
Button button = new Button(100, 50, 120, 30, "Rounded",
    new Color(52, 152, 219), Color.WHITE);
button.setRadius(8); // 8px rounded corners

// Containers
DivComponent panel = new DivComponent(50, 50, 300, 200);
panel.setBackgroundColor(Color.WHITE);
panel.setCornerRadius(12); // 12px rounded corners

// Fully rounded (pill shape)
Button pillButton = new Button(100, 100, 120, 30, "Pill",
    new Color(46, 204, 113), Color.WHITE);
pillButton.setRadius(15); // Half the height for pill shape
```

## Hover Effects

Add interactivity with hover animations:

```java
Button button = new Button(100, 50, 120, 30, "Hover Me",
    new Color(52, 152, 219), Color.WHITE);

// Parameters: duration (ms), hover background, hover text color
button.hover(200, new Color(41, 128, 185), Color.YELLOW);

// Subtle hover (just lighten the color)
button.hover(150, new Color(72, 172, 239), Color.WHITE);

// Dramatic hover (complete color change)
button.hover(300, new Color(231, 76, 60), Color.WHITE);
```

## Shadows and Depth

Create depth with text shadows:

```java
TextComponent heading = new TextComponent(50, 50, "Heading", 24, Color.WHITE);
// Text shadow is not directly available, but you can layer components

// For buttons
Button button = new Button(100, 50, 120, 30, "Shadowed",
    new Color(52, 152, 219), Color.WHITE);
button.setShadow(true); // Enables text shadow
```

## Transparency and Overlays

Create overlay effects with alpha values:

```java
// Semi-transparent overlay
DivComponent overlay = new DivComponent(0, 0, screenWidth, screenHeight);
overlay.setBackgroundColor(new Color(0, 0, 0, 180)); // Dark overlay

// Glass-morphism effect
DivComponent glassPanel = new DivComponent(50, 50, 300, 200);
glassPanel.setBackgroundColor(new Color(255, 255, 255, 40)); // Very transparent white
glassPanel.setOutline(new Color(255, 255, 255, 100), 1);
glassPanel.setCornerRadius(12);
```

## Design Patterns

### Card Design

```java
DivComponent card = new DivComponent(50, 50, 280, 180);
card.setBackgroundColor(Color.WHITE);
card.setCornerRadius(8);
card.setOutline(new Color(220, 220, 220), 1);

// Card header
TextComponent cardTitle = new TextComponent(20, 15, "Card Title", 16,
    new Color(44, 62, 80));
card.addChild(cardTitle);

// Card content
TextComponent cardText = new TextComponent(20, 45, "Card description...", 12,
    new Color(127, 140, 141));
card.addChild(cardText);

// Card action
Button cardButton = new Button(20, 130, 100, 30, "Action",
    new Color(52, 152, 219), Color.WHITE);
cardButton.setRadius(5);
card.addChild(cardButton);
```

### Alert/Notification Style

```java
// Success alert
DivComponent successAlert = new DivComponent(50, 50, 300, 50);
successAlert.setBackgroundColor(new Color(46, 204, 113, 230));
successAlert.setCornerRadius(6);
successAlert.setOutline(new Color(39, 174, 96), 2);

TextComponent successText = new TextComponent(15, 17, "✓ Success!", 14, Color.WHITE);
successAlert.addChild(successText);

// Error alert
DivComponent errorAlert = new DivComponent(50, 110, 300, 50);
errorAlert.setBackgroundColor(new Color(231, 76, 60, 230));
errorAlert.setCornerRadius(6);
errorAlert.setOutline(new Color(192, 57, 43), 2);

TextComponent errorText = new TextComponent(15, 17, "✗ Error occurred", 14, Color.WHITE);
errorAlert.addChild(errorText);
```

### Modal Dialog

```java
// Backdrop
DivComponent backdrop = new DivComponent(0, 0, screenWidth, screenHeight);
backdrop.setBackgroundColor(new Color(0, 0, 0, 150));

// Modal
DivComponent modal = new DivComponent(
    screenWidth / 2 - 200, screenHeight / 2 - 150,
    400, 300
);
modal.setBackgroundColor(Color.WHITE);
modal.setCornerRadius(12);
modal.setOutline(new Color(189, 195, 199), 1);

// Modal title
TextComponent modalTitle = new TextComponent(20, 20, "Confirm Action", 18,
    new Color(44, 62, 80));
modal.addChild(modalTitle);

// Modal buttons
Button confirmBtn = new Button(220, 240, 160, 40, "Confirm",
    new Color(46, 204, 113), Color.WHITE);
confirmBtn.setRadius(6);
modal.addChild(confirmBtn);

Button cancelBtn = new Button(20, 240, 180, 40, "Cancel",
    new Color(149, 165, 166), Color.WHITE);
cancelBtn.setRadius(6);
modal.addChild(cancelBtn);
```

## Best Practices

1. **Consistency**: Use a consistent color palette and font sizes throughout your UI
2. **Contrast**: Ensure text has enough contrast against backgrounds for readability
3. **Spacing**: Use adequate padding and margins between elements
4. **Feedback**: Provide visual feedback for interactive elements (hover effects, click animations)
5. **Performance**: Avoid excessive transparency and complex layering for better performance
6. **Accessibility**: Use readable font sizes (minimum 12px) and high contrast colors

## Next Steps

- Check out [Examples](/examples/basic-button) for practical implementations
- Learn about [Components](/api/components) and their styling options
