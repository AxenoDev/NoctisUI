package fr.libnaus.noctisui.client.component;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.render.font.FontAtlas;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class TextComponent implements UIComponent {

    @Getter private float x, y;
    @Getter private float fontSize;
    @Getter private String text;
    @Getter private Color color;
    @Getter private FontAtlas font;

    public TextComponent(float x, float y, String text, float fontSize, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontSize = fontSize;
        this.color = color;
        this.font = NoctisUIClient.getInstance().getFonts().getPoppins();
    }

    public TextComponent(float x, float y, String text, float fontSize, Color color, FontAtlas font) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontSize = fontSize;
        this.color = color;
        this.font = font != null ? font : NoctisUIClient.getInstance().getFonts().getPoppins();
    }

    public TextComponent setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public TextComponent setText(String text) {
        this.text = text;
        return this;
    }

    public TextComponent setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextComponent setColor(Color color) {
        this.color = color;
        return this;
    }

    public TextComponent setFont(FontAtlas font) {
        this.font = font;
        return this;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta) {
        if (font != null && text != null) {
            MatrixStack matrices = context.getMatrices();
            font.render(matrices, text, x, y, fontSize, color.getRGB());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

}
