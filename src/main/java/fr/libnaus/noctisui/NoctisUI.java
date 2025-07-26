package fr.libnaus.noctisui;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoctisUI implements ModInitializer {

    public static final String MODID = "noctisui";

    public static final Logger LOGGER = LogManager.getLogger("NoctisUI");

    @Getter
    private static NoctisUI instance;

    @Override
    public void onInitialize() {
        instance = this;
    }
}
