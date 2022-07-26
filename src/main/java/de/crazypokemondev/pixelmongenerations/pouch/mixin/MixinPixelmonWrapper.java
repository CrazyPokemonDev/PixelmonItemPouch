package de.crazypokemondev.pixelmongenerations.pouch.mixin;

import com.pixelmongenerations.common.battle.controller.participants.PixelmonWrapper;
import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(PixelmonWrapper.class)
public abstract class MixinPixelmonWrapper {

    @Redirect(method = "useItem", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;func_174925_a(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)I"), require = 1)
    private int clearMatchingItemsProxy(InventoryPlayer inventory, @Nullable Item itemIn, int metadataIn,
                                        int removeCount, @Nullable NBTTagCompound itemNBT) {
        int originalRemoveCount = removeCount;
        int i = inventory.clearMatchingItems(itemIn, metadataIn, removeCount, itemNBT);
        removeCount -= i;
        if (removeCount > 0) {
            for (int slot = 0; slot < inventory.mainInventory.size(); slot++) {
                ItemStack stack = inventory.mainInventory.get(slot);
                if (stack.hasCapability(Capabilities.ITEM_POUCH, null)) {
                    IPouchItemHandler pouch = Objects.requireNonNull(stack.getCapability(Capabilities.ITEM_POUCH, null));
                    i = pouch.clearMatchingItemsIgnoreNBT(itemIn, metadataIn, removeCount);
                    removeCount -= i;
                    if (inventory.player instanceof EntityPlayerMP && pouch instanceof ItemStackHandler) {
                        stack.setTagCompound(((ItemStackHandler) pouch).serializeNBT());
                        SPacketSetSlot updateItemPacket = new SPacketSetSlot(-2, slot, stack);
                        ((EntityPlayerMP) inventory.player).connection.sendPacket(updateItemPacket);
                    }
                    if (removeCount <= 0) break;
                }
            }
        }
        return originalRemoveCount - removeCount;
    }
}
