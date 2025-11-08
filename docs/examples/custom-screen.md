# Creating Custom Screens

Learn how to create custom screens in Minecraft using NoctisUI components.

## Basic Screen Structure

A basic Minecraft screen using NoctisUI:

```java
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import fr.libnaus.noctisui.client.component.Button;
import fr.libnaus.noctisui.client.component.DivComponent;
import fr.libnaus.noctisui.client.component.TextComponent;
import fr.libnaus.noctisui.client.utils.Color;

public class MyCustomScreen extends Screen {
    private DivComponent mainContainer;
    private Button closeButton;
    
    public MyCustomScreen() {
        super(Text.literal("My Custom Screen"));
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Initialize your UI components here
        initUI();
    }
    
    private void initUI() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Create main container
        mainContainer = new DivComponent(
            centerX - 200, centerY - 150,
            400, 300
        );
        mainContainer.setBackgroundColor(new Color(44, 62, 80));
        mainContainer.setCornerRadius(12);
        mainContainer.setOutline(new Color(52, 73, 94), 2);
        
        // Add title
        TextComponent title = new TextComponent(
            150, 20,
            "Welcome!",
            24,
            Color.WHITE
        );
        mainContainer.addChild(title);
        
        // Add close button
        closeButton = new Button(
            130, 240,
            140, 40,
            "Close",
            new Color(52, 152, 219),
            Color.WHITE
        );
        closeButton.setRadius(8);
        closeButton.hover(200, new Color(41, 128, 185), Color.WHITE);
        closeButton.setOnClick(b -> this.close());
        mainContainer.addChild(closeButton);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render background
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Render UI components
        if (mainContainer != null) {
            mainContainer.render(context, mouseX, mouseY, delta);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle component clicks
        if (mainContainer != null && mainContainer.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean shouldPause() {
        return false; // Set to true if you want the game to pause
    }
}
```

## Opening Your Screen

To open your custom screen:

```java
import net.minecraft.client.MinecraftClient;

// Open the screen
MinecraftClient.getInstance().setScreen(new MyCustomScreen());
```

You can trigger this from:
- A key binding
- A command
- A button click in another screen
- A network packet handler

## Advanced Screen Example

Here's a more complex example with multiple sections:

```java
public class AdvancedScreen extends Screen {
    private DivComponent sidebar;
    private DivComponent contentArea;
    private String selectedTab = "home";
    
    public AdvancedScreen() {
        super(Text.literal("Advanced Screen"));
    }
    
    @Override
    protected void init() {
        super.init();
        
        int margin = 20;
        int sidebarWidth = 180;
        int contentWidth = this.width - sidebarWidth - (margin * 3);
        int height = this.height - (margin * 2);
        
        // Create sidebar
        sidebar = new DivComponent(margin, margin, sidebarWidth, height);
        sidebar.setBackgroundColor(new Color(52, 73, 94));
        sidebar.setCornerRadius(10);
        
        // Sidebar title
        TextComponent sidebarTitle = new TextComponent(
            20, 20,
            "Menu",
            18,
            Color.WHITE
        );
        sidebar.addChild(sidebarTitle);
        
        // Menu buttons
        String[] tabs = {"Home", "Settings", "About"};
        int buttonY = 60;
        
        for (String tab : tabs) {
            Button tabButton = new Button(
                10, buttonY,
                sidebarWidth - 20, 35,
                tab,
                selectedTab.equalsIgnoreCase(tab) ? 
                    new Color(41, 128, 185) : new Color(52, 73, 94),
                Color.WHITE
            );
            tabButton.setRadius(6);
            tabButton.hover(150, new Color(41, 128, 185), Color.WHITE);
            
            String tabName = tab.toLowerCase();
            tabButton.setOnClick(b -> selectTab(tabName));
            
            sidebar.addChild(tabButton);
            buttonY += 45;
        }
        
        // Create content area
        contentArea = new DivComponent(
            margin * 2 + sidebarWidth,
            margin,
            contentWidth,
            height
        );
        contentArea.setBackgroundColor(new Color(236, 240, 241));
        contentArea.setCornerRadius(10);
        
        // Load initial content
        updateContent();
    }
    
    private void selectTab(String tab) {
        this.selectedTab = tab;
        this.init(this.client, this.width, this.height);
    }
    
    private void updateContent() {
        contentArea.clearChildren();
        
        switch (selectedTab) {
            case "home":
                loadHomeContent();
                break;
            case "settings":
                loadSettingsContent();
                break;
            case "about":
                loadAboutContent();
                break;
        }
    }
    
    private void loadHomeContent() {
        TextComponent header = new TextComponent(
            30, 30,
            "Welcome to NoctisUI",
            24,
            new Color(44, 62, 80)
        );
        contentArea.addChild(header);
        
        TextComponent description = new TextComponent(
            30, 70,
            "This is a demo screen showing NoctisUI capabilities.",
            14,
            new Color(127, 140, 141)
        );
        contentArea.addChild(description);
    }
    
    private void loadSettingsContent() {
        TextComponent header = new TextComponent(
            30, 30,
            "Settings",
            24,
            new Color(44, 62, 80)
        );
        contentArea.addChild(header);
        
        // Add settings controls here
    }
    
    private void loadAboutContent() {
        TextComponent header = new TextComponent(
            30, 30,
            "About",
            24,
            new Color(44, 62, 80)
        );
        contentArea.addChild(header);
        
        TextComponent version = new TextComponent(
            30, 70,
            "NoctisUI v1.0.0",
            14,
            new Color(127, 140, 141)
        );
        contentArea.addChild(version);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        if (sidebar != null) sidebar.render(context, mouseX, mouseY, delta);
        if (contentArea != null) contentArea.render(context, mouseX, mouseY, delta);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (sidebar != null && sidebar.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (contentArea != null && contentArea.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
```

## Responsive Design

Make your screen adapt to different window sizes:

```java
@Override
protected void init() {
    super.init();
    
    // Calculate responsive dimensions
    int maxWidth = Math.min(600, this.width - 40);
    int maxHeight = Math.min(400, this.height - 40);
    
    int x = (this.width - maxWidth) / 2;
    int y = (this.height - maxHeight) / 2;
    
    mainContainer = new DivComponent(x, y, maxWidth, maxHeight);
    // ... rest of initialization
}
```

## Handling Keyboard Input

Add keyboard support to your screen:

```java
@Override
public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    // ESC key to close
    if (keyCode == 256) { // GLFW.GLFW_KEY_ESCAPE
        this.close();
        return true;
    }
    
    return super.keyPressed(keyCode, scanCode, modifiers);
}
```

## Screen Transitions

Create smooth transitions between screens:

```java
public class TransitionHelper {
    public static void fadeToScreen(Screen currentScreen, Screen nextScreen) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Close current screen
        if (currentScreen != null) {
            currentScreen.close();
        }
        
        // Open next screen
        client.setScreen(nextScreen);
    }
}
```

## Best Practices

1. **Always call super methods**: Don't forget to call `super.init()`, `super.render()`, etc.
2. **Clean up resources**: Remove event listeners and clear references in `close()`
3. **Handle null checks**: Always check if components are initialized before using them
4. **Responsive layout**: Design for different screen sizes
5. **Accessibility**: Add keyboard navigation support
6. **Performance**: Don't create components in the render loop
7. **State management**: Keep track of your UI state properly

## Common Pitfalls

❌ **Don't do this:**
```java
@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    // Creating components in render - BAD!
    Button button = new Button(...);
    button.render(context, mouseX, mouseY, delta);
}
```

✅ **Do this instead:**
```java
private Button button;

@Override
protected void init() {
    super.init();
    // Create components once in init
    button = new Button(...);
}

@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    // Just render existing components
    button.render(context, mouseX, mouseY, delta);
}
```

## Next Steps

- Explore [Components API](/api/components) for all available components
- Check out [Container Layout](/examples/container-layout) for layout patterns
- Learn about [Styling](/guides/styling) to customize your screens
