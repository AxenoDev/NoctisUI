package fr.libnaus.noctisui.client.common;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.render.font.Fonts;
import net.minecraft.client.MinecraftClient;

public interface QuickImports
{
    MinecraftClient mc = MinecraftClient.getInstance();
    NoctisUIClient noctisui = NoctisUIClient.getInstance();
    Fonts fonts = noctisui.getFonts();
}
