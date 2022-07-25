package de.crazypokemondev.pixelmongenerations.pouch.common.events;

import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.common.items.ItemPouch;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttachCapabilitiesHandler {
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof ItemPouch && event.getCapabilities()
                .values().stream().noneMatch(x -> x instanceof ItemPouch.CapabilityProvider)) {
            event.addCapability(Capabilities.ITEM_POUCH_RESOURCE, new ItemPouch.CapabilityProvider());
        }
    }
}
