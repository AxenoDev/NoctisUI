package fr.libnaus.noctisui.client.component;

import net.minecraft.client.gui.DrawContext;

public interface UIComponent
{

    void render(DrawContext context, double mouseX, double mouseY, float delta);

    boolean mouseClicked(double mouseX, double mouseY, int button);

}
