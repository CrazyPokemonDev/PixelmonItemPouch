package de.crazypokemondev.pixelmongenerations.pouch.common.config;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;

public class PixelmonItemPouchConfig {
    private static final int CONFIG_FILE_INDEX = 0;

    public static class Pouch {
        public static int maxItemCount;
    }

    public static void load(BasicConfigManager configManager) {
        Pouch.maxItemCount = configManager.getConfigNode(CONFIG_FILE_INDEX, "Pouch", "MaxItemCount")
                .getInt();
    }
}
