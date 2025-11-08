package fr.libnaus.noctisui.client.component;

import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * A base abstract class for UI components.
 * Provides shared properties like position, size, visibility, and color.
 * <p>
 * Extend this class to create consistent, reusable UI components.
 *
 * @author axeno
 */
@Getter
@Setter
public abstract class UIBaseComponent implements UIComponent
{

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected boolean visible = true;
    protected boolean enabled = true;

    protected Color color = Color.WHITE;

    public UIBaseComponent(float x, float y, float width, float height)
    {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }

    public UIBaseComponent()
    {
        this(0, 0, 0, 0);
    }

    /**
     * Sets the position of the component.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     *
     * @return this instance (for chaining)
     */
    public UIBaseComponent setPosition(float x, float y)
    {
        this.x = x; this.y = y; return this;
    }

    /**
     * Sets the size of the component.
     *
     * @param width  Component width.
     * @param height Component height.
     *
     * @return this instance (for chaining)
     */
    public UIBaseComponent setSize(float width, float height)
    {
        this.width = width; this.height = height; return this;
    }

    /**
     * Sets both position and size at once.
     *
     * @param x      X coordinate.
     * @param y      Y coordinate.
     * @param width  Width.
     * @param height Height.
     *
     * @return this instance (for chaining)
     */
    public UIBaseComponent setBounds(float x, float y, float width, float height)
    {
        this.x = x; this.y = y; this.width = width; this.height = height; return this;
    }

    /**
     * Checks if a point (mouseX, mouseY) is inside this component.
     *
     * @param mouseX X coordinate of mouse.
     * @param mouseY Y coordinate of mouse.
     *
     * @return true if inside bounds.
     */
    public boolean contains(double mouseX, double mouseY)
    {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * Utility: toggle visibility.
     *
     * @return this instance.
     */
    public UIBaseComponent toggleVisibility()
    {
        this.visible = !this.visible; return this;
    }

    /**
     * Utility: toggle enabled state.
     *
     * @return this instance.
     */
    public UIBaseComponent toggleEnabled()
    {
        this.enabled = !this.enabled; return this;
    }
}
