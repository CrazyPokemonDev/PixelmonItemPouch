package de.crazypokemondev.pixelmongenerations.pouch.api.capabilities;

import de.crazypokemondev.pixelmongenerations.pouch.api.itempouch.Category;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IPouchItemHandler extends IItemHandlerModifiable {
    /**
     * Returns true if the item pouch can contain the given item stack.
     * Will also return false if the pouch already has reached its limit for this item!
     * @param stack The item stack in question
     * @return True if there still is space for at least some items from this stack
     */
    boolean canContain(ItemStack stack);

    /**
     * Removes items from the pouch, ignoring NBT tags
     * @param item The item type to remove
     * @param metadata The metadata (damage) of the item to remove
     * @param removeCount Maximum number of items to remove
     * @return Number of items removed
     */
    int clearMatchingItemsIgnoreNBT(Item item, int metadata, int removeCount);

    /**
     * Adds an item to the pouch.
     * Should only be called if there isn't already an ItemStack like this (ItemHandlerHelper.canItemStacksStack)
     * present in the pouch.
     * @param stack The stack to add.
     * @return The rest of the item stack, or {@link ItemStack}.EMPTY if it was fully inserted
     */
    ItemStack addItem(ItemStack stack);

    /**
     * Returns the number of slots currently in this item handler, not counting all stacks not of the given category
     * at the end.
     * @param category The category to use for filtering
     */
    int getSlotsOfCategoryTrimEnd(@NotNull Category category);

    /**
     * Returns the index of the first empty slot
     * @return The index of the first empty slot, or Optional.empty()
     */
    Optional<Integer> getFirstEmptySlot();

    /**
     * Gets the slot index for the nth stack, only counting items of the given category.
     * @param n The number of the slot, only counting items of the given category.
     * @param category The {@link Category} to count, or null to just remove empty stacks.
     * @return The index of the slot, if in range (counting all item stacks) or Optional.empty()
     */
    Optional<Integer> getSlotIndexForCategory(int n, @NotNull Category category);

    /**
     * Returns the number of slots that contain matching item stacks
     * @param category The category the items need to belong to
     */
    int getMatchingSlots(@NotNull Category category);
}
