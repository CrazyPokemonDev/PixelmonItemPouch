package de.crazypokemondev.pixelmongenerations.pouch.mixin;

import com.pixelmongenerations.core.network.packetHandlers.battles.BagPacket;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(BagPacket.class)
public abstract class MixinBagPacket {
    @Shadow(remap = false)
    int itemIndex;

    @ModifyVariable(method = "bagPacket", at = @At(value = "FIELD", args = "log=true",
            target = "Lnet/minecraft/entity/player/EntityPlayer;field_71071_by:Lnet/minecraft/entity/player/InventoryPlayer;",
            ordinal = 0, opcode = Opcodes.GETFIELD), remap = false, name = "usedStack", require = 1)
    private ItemStack onBagPacket(ItemStack usedStack, EntityPlayer player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.hasCapability(Capabilities.ITEM_POUCH, null)) {
                IPouchItemHandler pouch =
                        Objects.requireNonNull(stack.getCapability(Capabilities.ITEM_POUCH, null));
                for (int i = 0; i < pouch.getSlots(); i++) {
                    ItemStack innerStack = pouch.getStackInSlot(i);
                    if (Item.getIdFromItem(innerStack.getItem()) == this.itemIndex)
                        return innerStack;
                }
            }
        }
        return null;
    }

    @ModifyVariable(method = "bagPacketFromPokemon", at = @At(value = "FIELD", args = "log=true",
            target = "Lnet/minecraft/entity/player/EntityPlayer;field_71071_by:Lnet/minecraft/entity/player/InventoryPlayer;",
            ordinal = 0, opcode = Opcodes.GETFIELD), remap = false, name = "usedStack", require = 1)
    private ItemStack onBagPacketFromPokemon(ItemStack usedStack, EntityPlayer player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.hasCapability(Capabilities.ITEM_POUCH, null)) {
                IPouchItemHandler pouch =
                        Objects.requireNonNull(stack.getCapability(Capabilities.ITEM_POUCH, null));
                for (int i = 0; i < pouch.getSlots(); i++) {
                    ItemStack innerStack = pouch.getStackInSlot(i);
                    if (Item.getIdFromItem(innerStack.getItem()) == this.itemIndex)
                        return innerStack;
                }
            }
        }
        return null;
    }
}
