package de.crazypokemondev.pixelmongenerations.pouch;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.client.gui.GuiHandler;
import de.crazypokemondev.pixelmongenerations.pouch.common.items.ModItems;
import de.crazypokemondev.pixelmongenerations.pouch.common.config.PixelmonItemPouchConfig;
import de.crazypokemondev.pixelmongenerations.pouch.common.events.AttachCapabilitiesHandler;
import de.crazypokemondev.pixelmongenerations.pouch.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod(modid = PixelmonItemPouchMod.MOD_ID, version = PixelmonItemPouchMod.VERSION, name = PixelmonItemPouchMod.NAME)
public class PixelmonItemPouchMod {
    public static final String MOD_ID = "pixelmonitempouch";
    public static final String VERSION = "0.1.1";
    public static final String NAME = "PixelmonItemPouch";
    public static Logger logger;
    @SidedProxy(clientSide = "de.crazypokemondev.pixelmongenerations.pouch.proxy.ClientProxy",
            serverSide = "de.crazypokemondev.pixelmongenerations.pouch.proxy.ServerProxy")
    public static CommonProxy proxy;
    @Mod.Instance
    public static PixelmonItemPouchMod INSTANCE;

    public static BasicConfigManager configManager;
    public static Path configDir;

    public PixelmonItemPouchMod() {
        MinecraftForge.EVENT_BUS.register(new ModItems());
        MinecraftForge.EVENT_BUS.register(new AttachCapabilitiesHandler());
        configDir = ConfigUtils.checkDir(Paths.get("./config/" + MOD_ID));
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) throws IOException {
        logger = event.getModLog();

        String[] files = new String[]{"pixelmonitempouch.conf"};
        configManager = new BasicConfigManager(files, configDir, PixelmonItemPouchMod.class, NAME, MOD_ID, logger);
        configManager.init();
        PixelmonItemPouchConfig.load(configManager);

        Capabilities.register();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
    }
}
