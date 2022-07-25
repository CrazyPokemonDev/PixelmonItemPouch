package de.crazypokemondev.pixelmongenerations.pouch.proxy;

import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import de.crazypokemondev.pixelmongenerations.pouch.common.items.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PixelmonItemPouchMod.MOD_ID)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.initModels();
    }
}
