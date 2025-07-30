package fr.libnaus.noctisui.client.component;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.libnaus.noctisui.client.api.system.Render2DEngine;
import fr.libnaus.noctisui.client.common.QuickImports;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DivComponent implements QuickImports, UIComponent {

    @Getter
    private float x, y, width, height;

    @Getter
    private Color backgroundColor;

    @Getter
    private boolean hasBackground = false;

    @Getter
    private float cornerRadius = 0f;

    @Setter
    private boolean hasOutline = false;

    @Getter
    private Color outlineColor;

    @Getter
    private float outlineWidth;

    private boolean hasBlur = false;

    @Getter
    private float blurRadius;

    private Consumer<DivComponent> onClickAction;

    private final List<UIComponent> children = new ArrayList<>();

    private Runnable customRenderer;

    public DivComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public DivComponent withBackground(Color color) {
        this.backgroundColor = color;
        this.hasBackground = true;
        return this;
    }

    public DivComponent withCornerRadius(float radius) {
        this.cornerRadius = radius;
        return this;
    }

    public DivComponent withOutline(Color color, float width) {
        this.outlineColor = color;
        this.outlineWidth = width;
        this.hasOutline = true;
        return this;
    }

    public DivComponent withBlur(float radius) {
        this.blurRadius = radius;
        this.hasBlur = true;
        return this;
    }

    public DivComponent withBlur(float radius, float opacity) {
        this.blurRadius = radius;
        this.hasBlur = true;
        return this;
    }

    public DivComponent withCustomRenderer(Runnable renderer) {
        this.customRenderer = renderer;
        return this;
    }

    public DivComponent addChild(UIComponent child) {
        children.add(child);
        return this;
    }

    public DivComponent removeChild(UIComponent child) {
        children.remove(child);
        return this;
    }

    public void clearChildren() {
        children.clear();
    }

    /**
     * Sets the action to be performed when the button is clicked.
     *
     * @param action A Consumer that accepts the Button instance when clicked.
     * @return This Button instance for chaining.
     */
    public DivComponent setOnClick(Consumer<DivComponent> action) {
        this.onClickAction = action;
        return this;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (hasBackground && hasBlur) {
            Render2DEngine.drawBlurredRoundedRect(
                    matrices, x, y, width, height,
                    cornerRadius, blurRadius, blurOpacity, backgroundColor
            );
        }

        matrices.push();
        matrices.translate(x, y, 0);

        if (hasBackground && !hasBlur) {
            if (cornerRadius > 0) {
                Render2DEngine.drawRoundedRect(matrices, 0, 0, width, height, cornerRadius, backgroundColor);
                if (hasOutline)
                    Render2DEngine.drawRoundedOutline(matrices, 0, 0, width, height, cornerRadius, outlineWidth, outlineColor);
            } else {
                Render2DEngine.drawRect(matrices, 0, 0, width, height, backgroundColor);
                if (hasOutline)
                    Render2DEngine.drawOutline(matrices, 0, 0, width, height, outlineWidth, outlineColor);
            }
        }

        if (customRenderer != null) {
            customRenderer.run();
        }

        for (UIComponent child : children) {
            child.render(context, mouseX - x, mouseY - y, delta);
        }

        matrices.pop();
        RenderSystem.disableBlend();
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void onClick(float mouseX, float mouseY) {
        if (contains(mouseX, mouseY)) {
            if (onClickAction != null) {
                onClickAction.accept(this);
            }
            for (UIComponent child : children) {
                child.mouseClicked(mouseX - x, mouseY - y, 0);
            }
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean hasBackground() { return hasBackground; }

    public boolean hasOutline() { return hasOutline; }

    public boolean hasBlur() { return hasBlur; }

    public List<UIComponent> getChildren() { return new ArrayList<>(children); }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (contains(mouseX, mouseY)) {
            for (UIComponent child : children) {
                child.mouseClicked(mouseX - x, mouseY - y, 0);
            }
            return true;
        }
        return false;
    }
}