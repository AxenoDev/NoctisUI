package fr.libnaus.noctisui.client;

import fr.libnaus.noctisui.client.api.system.Shaders;
import fr.libnaus.noctisui.client.api.system.render.font.Fonts;
import fr.libnaus.noctisui.client.component.system.NotificationManager;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;

public class NoctisUIClient implements ClientModInitializer
{

    @Getter
    private static NoctisUIClient instance;

    @Getter
    private Fonts fonts;

    @Override
    public void onInitializeClient()
    {
        instance = this; this.fonts = new Fonts(); Shaders.load(); new NotificationManager();
        NotificationManager.init();
    }
}
