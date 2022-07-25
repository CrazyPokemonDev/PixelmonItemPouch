package de.crazypokemondev.pixelmongenerations.pouch.common.gui;

import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.client.gui.DisabledSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ItemPouchContainer extends Container {
    private static final int OFFSET_INVENTORY_X = 8;
    private static final int OFFSET_INVENTORY_Y = 144;
    private static final int OFFSET_HOTBAR_Y = 202;
    private final InventoryPlayer playerInventory;
    private final IPouchItemHandler pouch;

    private final IItemHandlerModifiable buffer;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = 36;

    public ItemPouchContainer(@NotNull IPouchItemHandler pouch, @NotNull InventoryPlayer playerInventory) {
        this.playerInventory = playerInventory;
        this.pouch = pouch;
        buffer = new BufferItemHandler();

        addPlayerSlots();
        addOwnSlots();
    }

    private void addPlayerSlots() {
        // main inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = OFFSET_INVENTORY_X + col * 18;
                int y = OFFSET_INVENTORY_Y + row * 18;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }
        // hotbar
        for (int col = 0; col < 9; col++) {
            int x = OFFSET_INVENTORY_X + col * 18;
            Slot slot = col == playerInventory.currentItem
                    ? new DisabledSlot(playerInventory, col, x, OFFSET_HOTBAR_Y)
                    : new Slot(playerInventory, col, x, OFFSET_HOTBAR_Y);
            this.addSlotToContainer(slot);
        }
    }

    @Override
    public void onContainerClosed(@NotNull EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (playerIn instanceof EntityPlayerMP) {
            ((EntityPlayerMP) playerIn).connection.sendPacket(
                    // this assumes the player is holding the pouch in their main hand
                    new SPacketEntityEquipment(playerIn.getEntityId(), EntityEquipmentSlot.MAINHAND,
                            playerIn.getHeldItem(EnumHand.MAIN_HAND)));
        }
    }

    private void addOwnSlots() {
        for (int i = 0; i < pouch.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(pouch, i, -500, -500));
        }
        this.addSlotToContainer(new SlotBufferIn(buffer, 0, -500, -500));
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return playerIn.inventory == playerInventory;
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack toReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            toReturn = stack.copy();
            if (index < PLAYER_INVENTORY_SLOT_COUNT) {
                if (!this.mergeItemStack(stack, PLAYER_INVENTORY_SLOT_COUNT, inventorySlots.size() - 1, false)
                        && !this.mergeItemStack(stack, inventorySlots.size() - 1, inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.mergeItemStack(stack, 0, PLAYER_INVENTORY_SLOT_COUNT, true))
                    return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return toReturn;
    }

    private void insertSlotIntoContainer(int index, Slot slot, ItemStack initialStack) {
        slot.slotNumber = index;
        inventorySlots.add(index, slot);
        inventoryItemStacks.add(index, initialStack);
        for (int i = index + 1; i < inventorySlots.size(); i++) {
            inventorySlots.get(i).slotNumber = i;
        }
    }

    private class BufferItemHandler extends ItemStackHandler {
        public BufferItemHandler() {
            super(1);
        }

        @Override
        public int getSlotLimit(int slot) {
            return pouch.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return pouch.canContain(stack);
        }
    }

    private class SlotBufferIn extends SlotItemHandler {
        public SlotBufferIn(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public void onSlotChanged() {
            if (this.getHasStack() && pouch.canContain(this.getStack())) {
                ItemStack result = pouch.addItem(this.getStack());
                int newIndex = pouch.getSlots() - 1;
                insertSlotIntoContainer(PLAYER_INVENTORY_SLOT_COUNT + newIndex,
                        new SlotItemHandler(pouch, newIndex, -500, -500),
                        pouch.getStackInSlot(newIndex));
                this.putStack(result);
            } else if (this.getHasStack()) {
                mergeItemStack(this.getStack(), 0, PLAYER_INVENTORY_SLOT_COUNT, true);
                this.putStack(ItemStack.EMPTY);
            }
            super.onSlotChanged();
        }

        @Override
        public boolean isItemValid(@NotNull ItemStack stack) {
            return pouch.canContain(stack);
        }
    }
}
