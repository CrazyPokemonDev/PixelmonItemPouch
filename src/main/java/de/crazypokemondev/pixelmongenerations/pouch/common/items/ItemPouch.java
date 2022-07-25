package de.crazypokemondev.pixelmongenerations.pouch.common.items;

import com.pixelmongenerations.core.config.PixelmonItemsPokeballs;
import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.client.gui.GuiHandler;
import de.crazypokemondev.pixelmongenerations.pouch.common.capabilities.PouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.common.config.PixelmonItemPouchConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities.ITEM_POUCH;

public class ItemPouch extends BaseItem {
    public ItemPouch() {
        super("item_pouch");
        setMaxStackSize(1);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn,
                                                             @NotNull EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND && !worldIn.isRemote) {
            playerIn.openGui(PixelmonItemPouchMod.INSTANCE, GuiHandler.ITEM_POUCH, worldIn, 0, 0, 0);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (worldIn.isRemote && stack.hasTagCompound() && stack.hasCapability(ITEM_POUCH, null)) {
            IPouchItemHandler pouch = Objects.requireNonNull(stack.getCapability(ITEM_POUCH, null));
            if (pouch instanceof ItemStackHandler) {
                ((ItemStackHandler) pouch).deserializeNBT(stack.getTagCompound());
            }
        }
    }

    public static class CapabilityProvider implements ICapabilitySerializable<NBTBase> {
        private final PouchItemHandler inventory;

        public CapabilityProvider() {
            this.inventory = new PouchItemHandler(PixelmonItemPouchConfig.Pouch.maxItemCount);
        }

        @Override
        public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == ITEM_POUCH;
        }

        @Nullable
        @Override
        public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == ITEM_POUCH ? ITEM_POUCH.cast(inventory) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return ITEM_POUCH.writeNBT(inventory, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            ITEM_POUCH.readNBT(inventory, null, nbt);
        }
    }
}
