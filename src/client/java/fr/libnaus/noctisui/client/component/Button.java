package fr.libnaus.noctisui.client.component;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.Render2DEngine;
import fr.libnaus.noctisui.client.api.system.render.font.FontAtlas;
import fr.libnaus.noctisui.client.common.QuickImports;
import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Consumer;

/**
 * A customizable button component for user interaction in the UI.
 *
 * <p>This component allows you to specify the position, size, label, colors, fonts, and various effects such as hover animations,
 * outlines, blur effects, shadows, and rounded corners.</p>
 *
 * <p>Buttons can respond to clicks via a {@link Consumer<Button>} callback.</p>
 *
 * <pre>
 * {@code
 * // Create a simple button
 * Button button = new Button(100, 40, 50, 50, "Click Me", new Color(0, 122, 204), Color.WHITE);
 *
 * // Set hover animation
 * button.hover(200, new Color(0, 150, 255), Color.YELLOW);
 *
 * // Set outline
 * button.setOutline(Color.BLACK, 2);
 *
 * // Set font and size
 * button.setFont(NoctisUIClient.getInstance().getFonts().getInterBold());
 * button.setFontSize(14);
 *
 * // Set corner radius
 * button.setRadius(8);
 *
 * // Set click action
 * button.setOnClick(b -> System.out.println("Button clicked!"));
 * }
 * </pre>
 *
 * <p>Buttons can be added to container components like {@link DivComponent}.</p>
 *
 * @author axeno
 */
@Getter
public class Button extends UIBaseComponent implements QuickImports
{

    @Getter
    @Setter
    private String label;
    @Setter
    private Color labelColor;
    @Setter
    private Color backgroundColor;

    private Color outlineColor = null;
    private float outlineWidth = 0;

    @Setter
    private FontAtlas font = NoctisUIClient.getInstance().getFonts().getInterMedium();

    @Setter
    private int fontSize = 9;

    @Setter
    private boolean shadow = false;

    private boolean hasHover = false;
    private long hoverAnimationDuration;
    private Color hoverBackgroundColor;
    private Color hoverLabelColor;

    private long hoverStartTime = -1;
    private boolean isHovered = false;

    @Setter
    private int radius = 5;

    private Consumer<Button> onClickAction;

    /**
     * Creates a new button instance.
     *
     * @param width           The width of the button.
     * @param height          The height of the button.
     * @param x               The X-coordinate of the button's top-left corner.
     * @param y               The Y-coordinate of the button's top-left corner.
     * @param label           The text label displayed on the button.
     * @param backgroundColor The background color of the button.
     * @param labelColor      The color of the button's label.
     */
    public Button(float x, float y, float width, float height, String label, Color backgroundColor, Color labelColor)
    {
        super(x, y, width, height); this.label = label; this.backgroundColor = backgroundColor;
        this.labelColor = labelColor;
    }

    /**
     * Sets the outline properties for the button.
     */
    public void setOutline(Color outlineColor, float outlineWidth)
    {
        this.outlineColor = outlineColor; this.outlineWidth = outlineWidth;
    }

    /**
     * Configures hover effects for the button.
     */
    public void hover(long animationDuration, Color hoverBackgroundColor, Color hoverLabelColor)
    {
        this.hasHover = true; this.hoverAnimationDuration = animationDuration;
        this.hoverBackgroundColor = hoverBackgroundColor; this.hoverLabelColor = hoverLabelColor;
    }

    /**
     * Sets the action to be performed when the button is clicked.
     */
    public void setOnClick(Consumer<Button> action)
    {
        this.onClickAction = action;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta)
    {
        MatrixStack matrices = context.getMatrices(); Color currentBackgroundColor = backgroundColor;
        Color currentLabelColor = labelColor;

        boolean isMouseOver = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        if (hasHover) {
            if (isMouseOver && !isHovered) {
                isHovered = true; hoverStartTime = System.currentTimeMillis();
            }
            else if (!isMouseOver && isHovered) {
                isHovered = false; hoverStartTime = System.currentTimeMillis();
            }

            if (hoverStartTime != -1) {
                long elapsed = System.currentTimeMillis() - hoverStartTime;
                float progress = Math.min(1f, (float) elapsed / hoverAnimationDuration);

                if (isHovered) {
                    currentBackgroundColor = Color.interpolateColor(backgroundColor, hoverBackgroundColor, progress);
                    currentLabelColor = Color.interpolateColor(labelColor, hoverLabelColor, progress);
                }
                else {
                    currentBackgroundColor = Color.interpolateColor(hoverBackgroundColor, backgroundColor, progress);
                    currentLabelColor = Color.interpolateColor(hoverLabelColor, labelColor, progress);
                    if (progress == 1f) hoverStartTime = -1;
                }
            }
        }

        Render2DEngine.drawRoundedRect(matrices, x, y, width, height, radius, currentBackgroundColor);

        if (outlineWidth > 0 && outlineColor != null)
            Render2DEngine.drawRoundedOutline(matrices, x, y, width, height, radius, outlineWidth, outlineColor);

        float textWidth = font.getWidth(label, fontSize); float textHeight = font.getLineHeight(fontSize);
        float textX = x + (width - textWidth) / 2; float textY = y + (height - textHeight) / 2;

        if (!shadow) {
            font.render(matrices, label, textX, textY, fontSize, currentLabelColor.getRGB());
        }
        else {
            font.renderWithShadow(matrices, label, textX, textY, fontSize, currentLabelColor.getRGB());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            if (onClickAction != null) {
                onClickAction.accept(this);
            } return true;
        } return false;
    }
}
