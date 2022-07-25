package de.crazypokemondev.pixelmongenerations.pouch.mixin;

import com.pixelmongenerations.core.network.packetHandlers.battles.UseItem;
import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(UseItem.Handler.class)
public abstract class MixinUseItem {
    // Mixin plugin fails to recognize attached sources and assumes incorrect mapping
    @Redirect(method = "onMessage(Lcom/pixelmongenerations/core/network/packetHandlers/battles/UseItem;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;func_174925_a(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)I"),
            require = 1, remap = false)
    private int clearMatchingItemsProxy(InventoryPlayer inventory, @Nullable Item itemIn, int metadataIn,
                                        int removeCount, @Nullable NBTTagCompound itemNBT) {
        int originalRemoveCount = removeCount;
        int i = inventory.clearMatchingItems(itemIn, metadataIn, removeCount, itemNBT);
        removeCount -= i;
        if (removeCount > 0) {
            for (ItemStack stack : inventory.mainInventory) {
                if (stack.hasCapability(Capabilities.ITEM_POUCH, null)) {
                    IPouchItemHandler pouch = Objects.requireNonNull(stack.getCapability(Capabilities.ITEM_POUCH, null));
                    i = pouch.clearMatchingItemsIgnoreNBT(itemIn, metadataIn, removeCount);
                    removeCount -= i;
                    if (removeCount <= 0) break;
                }
            }
        }
        return originalRemoveCount - removeCount;
    }
}
