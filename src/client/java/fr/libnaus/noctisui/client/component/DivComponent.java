package fr.libnaus.noctisui.client.component;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.libnaus.noctisui.client.api.system.Render2DEngine;
import fr.libnaus.noctisui.client.common.QuickImports;
import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * A versatile container component that can hold multiple {@link UIBaseComponent} children.
 * </p>
 * <p>
 * The {@code DivComponent} supports background rendering (with optional rounded corners),
 * outlines, click handling, and a custom render hook. It acts as a container resetting
 * its children's coordinate space to (0,0) relative to itself.
 * </p>
 *
 * <pre>
 * {@code
 * DivComponent div = new DivComponent(10, 10, 200, 100);
 * div.setBackgroundColor(new Color(0, 0, 0, 150));
 * div.setCornerRadius(10);
 * div.setOutline(Color.WHITE, 2);
 * div.setOnClick(c -> System.out.println("Div clicked!"));
 * div.addChild(new TextComponent(20, 20, "Hello, World!", 14, Color.WHITE));
 * }
 * </pre>
 *
 * @author axeno
 */
@Getter
public class DivComponent extends UIBaseComponent implements QuickImports
{

    @Setter
    private Color backgroundColor = null;
    private float cornerRadius = 0f;

    private Color outlineColor;
    private float outlineWidth = 0f;

    private Consumer<DivComponent> onClickAction;
    private final List<UIBaseComponent> children = new ArrayList<>();

    @Setter
    private Runnable customRenderer;

    /**
     * Creates a new {@code DivComponent} with the specified position and size.
     *
     * @param x      The x-coordinate of the component.
     * @param y      The y-coordinate of the component.
     * @param width  The width of the component.
     * @param height The height of the component.
     */
    public DivComponent(float x, float y, float width, float height)
    {
        super(x, y, width, height);
    }

    /**
     * Sets the corner radius for rounded corners.
     *
     * <pre>
     * {@code
     * div.setCornerRadius(12); // Rounded corners of 12 pixels
     * div.setCornerRadius(0);  // Sharp corners
     * }
     * </pre>
     *
     * @param radius The corner radius. Values below 0 are clamped to 0.
     */
    public void setCornerRadius(float radius)
    {
        this.cornerRadius = Math.max(0, radius);
    }

    /**
     * Sets the outline color and width for this component.
     *
     * <pre>
     * {@code
     * div.setOutline(Color.RED, 3);  // Red outline with 3px thickness
     * div.setOutline(Color.BLUE, 0); // Removes outline
     * }
     * </pre>
     *
     * @param color The outline color.
     * @param width The outline thickness in pixels. Values below 0 are clamped to 0.
     */
    public void setOutline(Color color, float width)
    {
        this.outlineColor = color; this.outlineWidth = Math.max(0, width);
    }

    /**
     * Defines an action to execute when this component is clicked.
     *
     * <pre>
     * {@code
     * div.setOnClick(c -> System.out.println("Clicked at: " + c.getX() + "," + c.getY()));
     * }
     * </pre>
     *
     * @param action A {@link Consumer} that receives this {@code DivComponent} when clicked.
     */
    public void setOnClick(Consumer<DivComponent> action)
    {
        this.onClickAction = action;
    }

    /**
     * Adds a single child component to this container.
     *
     * <pre>
     * {@code
     * div.addChild(new TextComponent(10, 10, "Hello", 14, Color.WHITE));
     * }
     * </pre>
     *
     * @param child The {@link UIBaseComponent} to add.
     */
    public void addChild(UIBaseComponent child)
    {
        children.add(child);
    }

    /**
     * Adds multiple child components to this container at once.
     *
     * <pre>
     * {@code
     * div.addChildren(
     *     new TextComponent(10, 10, "Hello", 14, Color.WHITE),
     *     new ImageComponent(20, 20, 50, 50, texture)
     * );
     * }
     * </pre>
     *
     * @param children The {@link UIBaseComponent} instances to add.
     */
    public void addChildren(UIBaseComponent... children)
    {
        for (UIBaseComponent child : children)
            addChild(child);
    }

    /**
     * Removes a specific child component from this container.
     *
     * <pre>
     * {@code
     * div.removeChild(textComponent);
     * }
     * </pre>
     *
     * @param child The {@link UIBaseComponent} to remove.
     */
    public void removeChild(UIBaseComponent child)
    {
        children.remove(child);
    }

    /**
     * Removes all child components that match the given filter condition.
     * <p>
     * This method allows you to dynamically clean or filter child elements
     * based on custom logic.
     * </p>
     *
     * <pre>
     * {@code
     * // Remove all invisible components
     * div.removeIf(child -> !child.isVisible());
     *
     * // Remove all text-based components
     * div.removeIf(child -> child instanceof TextComponent);
     * }
     * </pre>
     *
     * @param filter A predicate determining which children to remove.
     *               Components returning {@code true} are removed.
     */
    public void removeIf(Predicate<UIBaseComponent> filter)
    {
        children.removeIf(filter);
    }

    /**
     * Removes all children from this component.
     *
     * <pre>
     * {@code
     * div.clearChildren(); // Removes all child components
     * }
     * </pre>
     */
    public void clearChildren()
    {
        children.clear();
    }

    /**
     * Renders this container, its background, outline, and all visible children.
     *
     * @param context The {@link DrawContext} used for rendering.
     * @param mouseX  The current mouse X position.
     * @param mouseY  The current mouse Y position.
     * @param delta   Partial tick delta for animations or transitions.
     */
    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta)
    {
        if (!visible) return;

        MatrixStack matrices = context.getMatrices(); RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();

        matrices.push(); matrices.translate(x, y, 0);

        if (backgroundColor != null) {
            if (cornerRadius > 0) {
                Render2DEngine.drawRoundedRect(matrices, 0, 0, width, height, cornerRadius, backgroundColor);
                if (outlineWidth > 0)
                    Render2DEngine.drawRoundedOutline(matrices, 0, 0, width, height, cornerRadius, outlineWidth, outlineColor);
            }
            else {
                Render2DEngine.drawRect(matrices, 0, 0, width, height, backgroundColor); if (outlineWidth > 0)
                    Render2DEngine.drawOutline(matrices, 0, 0, width, height, outlineWidth, outlineColor);
            }
        }

        if (customRenderer != null) customRenderer.run();

        for (UIBaseComponent child : children) {
            if (!child.isVisible() || !child.enabled) continue; child.render(context, mouseX - x, mouseY - y, delta);
        }

        matrices.pop(); RenderSystem.disableBlend();
    }

    /**
     * Determines whether the given mouse coordinates are inside this component.
     *
     * @param mouseX The X coordinate of the mouse.
     * @param mouseY The Y coordinate of the mouse.
     *
     * @return {@code true} if the mouse is within bounds; otherwise {@code false}.
     */
    public boolean contains(double mouseX, double mouseY)
    {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * Handles a click interaction within this component.
     * <p>
     * If the click occurs within the component’s bounds, the click action (if defined)
     * is executed and the event is propagated to all child components.
     * </p>
     *
     * <pre>
     * {@code
     * div.onClick(mouseX, mouseY); // Triggers click action if inside bounds
     * }
     * </pre>
     *
     * @param mouseX The x-coordinate of the mouse click.
     * @param mouseY The y-coordinate of the mouse click.
     */
    public void onClick(float mouseX, float mouseY)
    {
        if (contains(mouseX, mouseY)) {
            if (onClickAction != null) onClickAction.accept(this);

            for (UIComponent child : children)
                child.mouseClicked(mouseX - x, mouseY - y, 0);
        }
    }

    /**
     * Called by Minecraft’s GUI system when a mouse button is pressed.
     * Handles internal click logic and propagates the event to children.
     *
     * @param mouseX The mouse X position.
     * @param mouseY The mouse Y position.
     * @param button The mouse button index.
     *
     * @return Always returns {@code false} to allow event propagation.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (!visible || !contains(mouseX, mouseY)) return false;

        if (onClickAction != null) onClickAction.accept(this);

        children.forEach(child -> child.mouseClicked(mouseX - x, mouseY - y, button)); return false;
    }
}
