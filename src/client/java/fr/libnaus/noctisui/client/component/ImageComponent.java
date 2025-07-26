package fr.libnaus.noctisui.client.component;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Getter
public class ImageComponent implements UIComponent {

    private int x, y, width, height;
    private Identifier texture;

    public ImageComponent(int x, int y, int width, int height, Identifier texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public ImageComponent setTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public ImageComponent setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ImageComponent setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta) {
        if (texture != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.texParameter(3553, 10241, 9729);
            RenderSystem.texParameter(3553, 10240, 9729);
            context.drawTexture(texture, x, y, 0, 0, width, height, width, height);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

}
