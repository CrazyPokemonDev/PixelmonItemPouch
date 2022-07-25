package de.crazypokemondev.pixelmongenerations.pouch.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemPouch ITEM_POUCH = new ItemPouch();

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ITEM_POUCH
        );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ITEM_POUCH.initModel();
    }
}
