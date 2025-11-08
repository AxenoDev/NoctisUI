package fr.libnaus.noctisui.client.component;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.render.font.FontAtlas;
import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

/**
 * A simple text component for rendering text in the UI.
 *
 * <p>This component allows you to specify the position, font size, text content, color, and font of the text to be rendered.</p>
 *
 * <pre>
 * {@code
 * // Create a basic text component
 * TextComponent textComponent = new TextComponent(50, 50, "Hello, World!", 16, Color.WHITE);
 *
 * // Create a text component with a custom font
 * FontAtlas customFont = NoctisUIClient.getInstance().getFonts().getPoppins();
 * TextComponent customText = new TextComponent(100, 100, "Custom Font", 18, Color.CYAN, customFont);
 * }
 * </pre>
 *
 * <p>TextComponents are typically used as children of {@link DivComponent} or other container components.</p>
 *
 * @author axeno
 */
@Getter
@Setter
public class TextComponent extends UIBaseComponent
{

    private float fontSize;
    private String text;
    private FontAtlas font;

    /**
     * Creates a new TextComponent with the specified parameters and a custom font.
     *
     * @param x        The X-coordinate of the text position.
     * @param y        The Y-coordinate of the text position.
     * @param text     The text content to be displayed.
     * @param fontSize The size of the font.
     * @param color    The color of the text.
     * @param font     The FontAtlas to use for rendering the text.
     *
     *                 <pre>
     *                                 {@code
     *                                 FontAtlas customFont = NoctisUIClient.getInstance().getFonts().getPoppins();
     *                                 TextComponent text = new TextComponent(20, 20, "Custom", 14, Color.RED, customFont);
     *                                 }
     *                                 </pre>
     */
    public TextComponent(float x, float y, String text, float fontSize, Color color, FontAtlas font)
    {
        super(x, y, 0, 0); this.text = text; this.fontSize = fontSize; this.color = color;
        this.font = font != null ? font : NoctisUIClient.getInstance().getFonts().getPoppins();
    }

    /**
     * Creates a new TextComponent with the specified parameters using the default font.
     *
     * @param x        The X-coordinate of the text position.
     * @param y        The Y-coordinate of the text position.
     * @param text     The text content to be displayed.
     * @param fontSize The size of the font.
     * @param color    The color of the text.
     *
     *                 <pre>
     *                                 {@code
     *                                 TextComponent text = new TextComponent(50, 50, "Hello World", 16, Color.WHITE);
     *                                 }
     *                                 </pre>
     */
    public TextComponent(float x, float y, String text, float fontSize, Color color)
    {
        this(x, y, text, fontSize, color, NoctisUIClient.getInstance().getFonts().getPoppins());
    }

    /**
     * Renders the text at its current position with the current font and color.
     *
     * @param context The DrawContext used for rendering.
     * @param mouseX  The current mouse X position.
     * @param mouseY  The current mouse Y position.
     * @param delta   Partial tick delta for animations or transitions.
     */
    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta)
    {
        if (font == null || text == null) return;

        MatrixStack matrices = context.getMatrices(); font.render(matrices, text, x, y, fontSize, color.getRGB());
    }

    /**
     * TextComponents do not handle mouse clicks by default.
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
