package fr.libnaus.noctisui.client.component;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

/**
 * A UI component that displays an image at a specified position and size.
 *
 * <p>This component allows you to set the texture, position, and size of the image to be rendered.</p>
 *
 * <pre>
 * {@code
 * // Create an ImageComponent at position (50, 50) with size 100x100
 * Identifier textureId = new Identifier("modid", "textures/gui/image.png");
 * ImageComponent imageComponent = new ImageComponent(50, 50, 100, 100, textureId);
 *
 * // Change the texture later
 * imageComponent.setTexture(new Identifier("modid", "textures/gui/other_image.png"));
 * }
 * </pre>
 *
 * <p>ImageComponents can be added as children to container components like {@link DivComponent}.</p>
 *
 * @author axeno
 */
@Getter
@Setter
public class ImageComponent extends UIBaseComponent
{

    private Identifier texture;

    /**
     * Creates a new ImageComponent with the specified position, size, and texture.
     *
     * @param x       The X-coordinate of the image position.
     * @param y       The Y-coordinate of the image position.
     * @param width   The width of the image.
     * @param height  The height of the image.
     * @param texture The Identifier of the texture to be displayed.
     *
     *                <pre>
     *                {@code
     *                Identifier texture = new Identifier("modid", "textures/gui/image.png");
     *                ImageComponent image = new ImageComponent(10, 10, 64, 64, texture);
     *                }
     *                </pre>
     */
    public ImageComponent(int x, int y, int width, int height, Identifier texture)
    {
        super(x, y, width, height); this.texture = texture;
    }

    /**
     * Renders the image at its current position and size.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The current mouse X position.
     * @param mouseY  The current mouse Y position.
     * @param delta   Partial tick delta for animations or transitions.
     */
    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta)
    {
        if (texture != null) {
            RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc(); RenderSystem.setShaderTexture(0, texture);
            RenderSystem.texParameter(3553, 10241, 9729); RenderSystem.texParameter(3553, 10240, 9729);
            context.drawTexture(texture, (int) x, (int) y, 0, 0, (int) width, (int) height, (int) width, (int) height);
            RenderSystem.disableBlend();
        }
    }

    /**
     * ImageComponents do not handle mouse clicks by default.
     *
     * @param mouseX The mouse X position.
     * @param mouseY The mouse Y position.
     * @param button The mouse button index.
     *
     * @return Always returns false.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return false;
    }

}
