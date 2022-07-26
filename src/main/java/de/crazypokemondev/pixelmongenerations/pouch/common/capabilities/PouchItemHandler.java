package de.crazypokemondev.pixelmongenerations.pouch.common.capabilities;

import com.pixelmongenerations.common.item.PixelmonItem;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.api.itempouch.Category;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PouchItemHandler extends ItemStackHandler implements IPouchItemHandler {
    private final int maxStackSize;

    public PouchItemHandler(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public int getSlotLimit(int slot) {
        return maxStackSize;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return canContain(stack) && canItemStacksStackIncludeEmpty(stack, stacks.get(slot));
    }

    @Override
    public int clearMatchingItemsIgnoreNBT(Item item, int metadata, int removeCount) {
        int amount = 0;
        for (ItemStack stack : stacks) {
            if (stack.getItem() == item && stack.getMetadata() == metadata) {
                int stackCount = stack.getCount();
                amount = Math.min(removeCount, stackCount);
                stack.setCount(stackCount - amount);
                break;
            }
        }
        return amount;
    }

    @Override
    public boolean canContain(ItemStack stack) {
        return stack.getItem() instanceof PixelmonItem &&  stacks.stream().noneMatch(s -> canItemStacksStackIncludeEmpty(stack, s) && s.getCount() >= maxStackSize);
    }

    @Override
    public ItemStack addItem(ItemStack stack) {
        if (!canContain(stack)) return stack;
        int stackCount = stack.getCount();
        int insertCount = Math.min(stackCount, maxStackSize);
        ItemStack insert = stack.copy();
        insert.setCount(insertCount);
        NonNullList<ItemStack> oldStacks = stacks;
        setSize(oldStacks.size() + 1);
        for (int i = 0; i < oldStacks.size(); i++) {
            stacks.set(i, oldStacks.get(i));
        }
        stacks.set(oldStacks.size(), insert);
        ItemStack copy = stack.copy();
        copy.setCount(stackCount - insertCount);
        return copy;
    }

    @Override
    public Optional<Integer> getSlotIndexForCategory(int n, @NotNull Category category) {
        int slotsSkipped = 0;
        for (int i = 0; i <= n; i++) {
            if (i + slotsSkipped >= stacks.size()) return Optional.empty();
            while (!category.containsItem(stacks.get(i + slotsSkipped).getItem())) {
                slotsSkipped++;
                if (i + slotsSkipped >= stacks.size()) return Optional.empty();
            }
        }
        return Optional.of(n + slotsSkipped);
    }

    @Override
    public Optional<Integer> getFirstEmptySlot() {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isEmpty()) return Optional.of(i);
        }
        return Optional.empty();
    }

    @Override
    public int getMatchingSlots(@NotNull Category category) {
        return (int) stacks.stream().filter(x -> category.containsItem(x.getItem())).count();
    }

    private boolean canItemStacksStackIncludeEmpty(ItemStack stack1, ItemStack stack2) {
        return stack1.isEmpty() || stack2.isEmpty() || ItemHandlerHelper.canItemStacksStack(stack1, stack2);
    }
}
