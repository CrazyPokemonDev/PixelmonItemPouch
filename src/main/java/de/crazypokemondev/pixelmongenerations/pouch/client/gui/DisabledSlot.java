package de.crazypokemondev.pixelmongenerations.pouch.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class DisabledSlot extends Slot {
    public DisabledSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
