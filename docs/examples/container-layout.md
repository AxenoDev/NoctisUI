# Container Layout Example

This example demonstrates how to use `DivComponent` to create organized layouts.

## Basic Container

A simple container with a background and children:

```java
import fr.libnaus.noctisui.client.component.DivComponent;
import fr.libnaus.noctisui.client.component.TextComponent;
import fr.libnaus.noctisui.client.component.Button;
import fr.libnaus.noctisui.client.utils.Color;

public class MyScreen extends Screen {
    private DivComponent container;
    
    @Override
    protected void init() {
        super.init();
        
        // Create a container
        container = new DivComponent(50, 50, 300, 200);
        
        // Style the container
        container.setBackgroundColor(new Color(44, 62, 80)); // Dark blue-gray
        container.setCornerRadius(10);
        container.setOutline(new Color(52, 73, 94), 2);
        
        // Add children (positions are relative to container)
        TextComponent title = new TextComponent(20, 15, "My Panel", 18, Color.WHITE);
        container.addChild(title);
        
        Button okButton = new Button(20, 150, 100, 30, "OK", 
            new Color(46, 204, 113), Color.WHITE);
        container.addChild(okButton);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        container.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (container.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
```

## Settings Panel Example

A more complex example showing a settings panel:

```java
public class SettingsPanel {
    private DivComponent panel;
    
    public SettingsPanel(int x, int y) {
        // Main panel
        panel = new DivComponent(x, y, 400, 350);
        panel.setBackgroundColor(new Color(44, 62, 80));
        panel.setCornerRadius(12);
        panel.setOutline(new Color(52, 73, 94), 2);
        
        // Header section
        DivComponent header = new DivComponent(0, 0, 400, 50);
        header.setBackgroundColor(new Color(52, 73, 94));
        header.setCornerRadius(12); // Will only show on top corners
        
        TextComponent title = new TextComponent(20, 15, "Settings", 20, Color.WHITE,
            NoctisUIClient.getInstance().getFonts().getInterBold());
        header.addChild(title);
        panel.addChild(header);
        
        // Content section
        int contentY = 70;
        
        // Sound option
        TextComponent soundLabel = new TextComponent(20, contentY, "Sound:", 14, Color.WHITE);
        panel.addChild(soundLabel);
        
        Button soundToggle = new Button(300, contentY - 5, 80, 25, "ON",
            new Color(46, 204, 113), Color.WHITE);
        soundToggle.setRadius(5);
        soundToggle.setOnClick(b -> {
            boolean isOn = b.getLabel().equals("ON");
            b.setLabel(isOn ? "OFF" : "ON");
            b.setBackgroundColor(isOn ? 
                new Color(231, 76, 60) : new Color(46, 204, 113));
        });
        panel.addChild(soundToggle);
        
        contentY += 40;
        
        // Volume option
        TextComponent volumeLabel = new TextComponent(20, contentY, "Volume:", 14, Color.WHITE);
        panel.addChild(volumeLabel);
        
        contentY += 60;
        
        // Difficulty option
        TextComponent difficultyLabel = new TextComponent(20, contentY, "Difficulty:", 14, Color.WHITE);
        panel.addChild(difficultyLabel);
        
        String[] difficulties = {"Easy", "Normal", "Hard"};
        int buttonWidth = 90;
        int buttonX = 150;
        
        for (String difficulty : difficulties) {
            Button diffButton = new Button(buttonX, contentY - 5, buttonWidth, 25,
                difficulty, new Color(52, 152, 219), Color.WHITE);
            diffButton.setRadius(5);
            diffButton.hover(150, new Color(41, 128, 185), Color.WHITE);
            diffButton.setOnClick(b -> System.out.println("Selected: " + difficulty));
            panel.addChild(diffButton);
            buttonX += buttonWidth + 5;
        }
        
        // Footer buttons
        Button saveButton = new Button(30, 300, 160, 35, "Save & Close",
            new Color(46, 204, 113), Color.WHITE);
        saveButton.setRadius(8);
        saveButton.hover(200, new Color(52, 211, 124), Color.WHITE);
        saveButton.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());
        panel.addChild(saveButton);
        
        Button cancelButton = new Button(210, 300, 160, 35, "Cancel",
            new Color(149, 165, 166), Color.WHITE);
        cancelButton.setRadius(8);
        cancelButton.hover(200, new Color(127, 140, 141), Color.WHITE);
        cancelButton.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());
        panel.addChild(cancelButton);
    }
    
    public void render(DrawContext context, double mouseX, double mouseY, float delta) {
        panel.render(context, mouseX, mouseY, delta);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return panel.mouseClicked(mouseX, mouseY, button);
    }
}
```

## Nested Containers

Containers can contain other containers for complex layouts:

```java
DivComponent mainContainer = new DivComponent(50, 50, 500, 400);
mainContainer.setBackgroundColor(new Color(236, 240, 241));
mainContainer.setCornerRadius(10);

// Left sidebar
DivComponent sidebar = new DivComponent(10, 10, 150, 380);
sidebar.setBackgroundColor(new Color(52, 73, 94));
sidebar.setCornerRadius(8);

TextComponent sidebarTitle = new TextComponent(15, 15, "Menu", 16, Color.WHITE);
sidebar.addChild(sidebarTitle);

// Add sidebar to main container
mainContainer.addChild(sidebar);

// Right content area
DivComponent contentArea = new DivComponent(170, 10, 320, 380);
contentArea.setBackgroundColor(Color.WHITE);
contentArea.setCornerRadius(8);

TextComponent contentTitle = new TextComponent(20, 20, "Content", 18, 
    new Color(44, 62, 80));
contentArea.addChild(contentTitle);

// Add content area to main container
mainContainer.addChild(contentArea);
```

## Scrollable Content

While NoctisUI doesn't have built-in scrolling, you can manage it manually:

```java
public class ScrollablePanel {
    private DivComponent panel;
    private DivComponent contentContainer;
    private float scrollOffset = 0;
    private final float maxScroll = 500; // Total content height
    
    public ScrollablePanel(int x, int y, int width, int height) {
        panel = new DivComponent(x, y, width, height);
        panel.setBackgroundColor(new Color(255, 255, 255, 200));
        panel.setCornerRadius(8);
        
        // Create content that may be taller than the panel
        contentContainer = new DivComponent(0, (int) scrollOffset, width - 10, 600);
        
        // Add lots of content
        for (int i = 0; i < 20; i++) {
            TextComponent item = new TextComponent(10, 10 + (i * 30), 
                "Item " + (i + 1), 12, new Color(44, 62, 80));
            contentContainer.addChild(item);
        }
        
        panel.addChild(contentContainer);
    }
    
    public void scroll(double amount) {
        scrollOffset += amount * 10; // Scroll speed
        scrollOffset = Math.max(-maxScroll + panel.getHeight(), 
                               Math.min(0, scrollOffset));
        contentContainer.setY(scrollOffset);
    }
}
```

## Dynamic Content

Add and remove children dynamically:

```java
DivComponent listContainer = new DivComponent(50, 50, 300, 400);
listContainer.setBackgroundColor(new Color(255, 255, 255));

// Add items dynamically
public void addItem(String text) {
    int itemCount = listContainer.getChildren().size();
    int yPos = itemCount * 40 + 10;
    
    DivComponent item = new DivComponent(10, yPos, 280, 35);
    item.setBackgroundColor(new Color(236, 240, 241));
    item.setCornerRadius(5);
    
    TextComponent label = new TextComponent(10, 10, text, 12, 
        new Color(44, 62, 80));
    item.addChild(label);
    
    Button removeBtn = new Button(230, 5, 40, 25, "X", 
        new Color(231, 76, 60), Color.WHITE);
    removeBtn.setRadius(3);
    removeBtn.setOnClick(b -> listContainer.removeChild(item));
    item.addChild(removeBtn);
    
    listContainer.addChild(item);
}

// Remove all items
public void clearList() {
    listContainer.clearChildren();
}

// Remove items matching a condition
public void removeCompletedItems() {
    listContainer.removeIf(child -> {
        // Your condition here
        return false; // Example
    });
}
```

## Next Steps

- Learn about [Custom Styling](/guides/styling)
- Check out the [Components API](/api/components) for all available methods
