package de.crazypokemondev.pixelmongenerations.pouch.api.capabilities;

import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    @CapabilityInject(IPouchItemHandler.class)
    public static Capability<IPouchItemHandler> ITEM_POUCH;
    public static final ResourceLocation ITEM_POUCH_RESOURCE =
            new ResourceLocation(PixelmonItemPouchMod.MOD_ID, "item_pouch");

    public static void register() {
        CapabilityManager.INSTANCE.register(IPouchItemHandler.class,
                new PouchItemHandlerStorage(), new PouchItemHandlerFactory());
    }
}
