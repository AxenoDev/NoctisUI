# Basic Button Example

This example shows how to create a simple button with NoctisUI.

## Simple Button

The most basic button you can create:

```java
import fr.libnaus.noctisui.client.component.Button;
import fr.libnaus.noctisui.client.utils.Color;

public class MyScreen extends Screen {
    private Button myButton;
    
    public MyScreen() {
        super(Text.literal("My Screen"));
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Create a button
        myButton = new Button(
            100, 50,              // x, y position
            120, 30,              // width, height
            "Click Me!",          // button label
            new Color(0, 122, 204),    // background color (blue)
            Color.WHITE           // text color
        );
        
        // Add click handler
        myButton.setOnClick(button -> {
            System.out.println("Button clicked!");
        });
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Render the button
        myButton.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle button clicks
        if (myButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
```

## Button with Hover Effect

Add a smooth hover animation:

```java
Button hoverButton = new Button(100, 100, 120, 30, "Hover Me",
    new Color(41, 128, 185), Color.WHITE);

// Configure hover animation
// Parameters: duration (ms), hover background color, hover text color
hoverButton.hover(
    200,                          // 200ms animation
    new Color(52, 152, 219),     // lighter blue on hover
    new Color(255, 255, 255)     // white text
);

hoverButton.setOnClick(b -> {
    System.out.println("Hover button clicked!");
});
```

## Styled Button

Create a button with custom styling:

```java
Button styledButton = new Button(100, 150, 150, 40, "Styled Button",
    new Color(46, 204, 113), Color.WHITE);

// Add rounded corners
styledButton.setRadius(8);

// Add an outline
styledButton.setOutline(new Color(39, 174, 96), 2);

// Use a custom font
styledButton.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());
styledButton.setFontSize(14);

// Enable text shadow
styledButton.setShadow(true);

// Add hover effect
styledButton.hover(150, new Color(52, 211, 124), Color.YELLOW);
```

## Multiple Buttons

Create several buttons with different actions:

```java
public class ButtonDemoScreen extends Screen {
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 50;
        int buttonWidth = 120;
        int buttonHeight = 35;
        int spacing = 10;
        
        // Save button (green)
        saveButton = new Button(
            centerX - buttonWidth - spacing, startY,
            buttonWidth, buttonHeight,
            "Save",
            new Color(46, 204, 113), Color.WHITE
        );
        saveButton.setRadius(6);
        saveButton.hover(200, new Color(52, 211, 124), Color.WHITE);
        saveButton.setOnClick(b -> handleSave());
        
        // Cancel button (gray)
        cancelButton = new Button(
            centerX + spacing, startY,
            buttonWidth, buttonHeight,
            "Cancel",
            new Color(149, 165, 166), Color.WHITE
        );
        cancelButton.setRadius(6);
        cancelButton.hover(200, new Color(127, 140, 141), Color.WHITE);
        cancelButton.setOnClick(b -> handleCancel());
        
        // Delete button (red)
        deleteButton = new Button(
            centerX - buttonWidth / 2, startY + buttonHeight + spacing,
            buttonWidth, buttonHeight,
            "Delete",
            new Color(231, 76, 60), Color.WHITE
        );
        deleteButton.setRadius(6);
        deleteButton.hover(200, new Color(192, 57, 43), Color.WHITE);
        deleteButton.setOnClick(b -> handleDelete());
    }
    
    private void handleSave() {
        System.out.println("Saving...");
        this.close();
    }
    
    private void handleCancel() {
        System.out.println("Cancelled");
        this.close();
    }
    
    private void handleDelete() {
        System.out.println("Deleting...");
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        saveButton.render(context, mouseX, mouseY, delta);
        cancelButton.render(context, mouseX, mouseY, delta);
        deleteButton.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (saveButton.mouseClicked(mouseX, mouseY, button)) return true;
        if (cancelButton.mouseClicked(mouseX, mouseY, button)) return true;
        if (deleteButton.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
```

## Next Steps

- Learn about [Container Layouts](/examples/container-layout) to organize buttons
- Explore [Custom Styling](/guides/styling) for more design options
- Check out the [Components API](/api/components) for all available methods
