package de.crazypokemondev.pixelmongenerations.pouch.client.gui;

import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.api.itempouch.Category;
import de.crazypokemondev.pixelmongenerations.pouch.common.gui.ItemPouchContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Optional;

public class ItemPouchGuiContainer extends GuiContainer {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 226;
    private static final int OFFSET_X = 8;  // text starts at OFFSET_X + 19
    private static final int ITEM_POUCH_AREA_WIDTH = 158;
    private static final int OFFSET_Y = 8;  // next row starts at +18
    private static final int SCROLL_BAR_X_BACKGROUND = 176;
    private static final int SCROLL_BAR_X_FOREGROUND = 178;
    private static final int SCROLL_BAR_Y = 0;
    private static final int SCROLL_BAR_WIDTH = 2;
    private static final int SCROLL_BAR_HEIGHT = 104;
    private static final int NUM_ROWS = 6;
    private static final ResourceLocation background =
            new ResourceLocation(PixelmonItemPouchMod.MOD_ID, "textures/gui/container/item_pouch.png");
    private final IPouchItemHandler pouch;
    private int pouchSlotOffset = 0;
    @Nullable
    private Category selectedCategory = null;

    public ItemPouchGuiContainer(Container container, IPouchItemHandler pouch) {
        super(container);

        this.pouch = pouch;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int nonEmptySlots = selectedCategory == null
                ? Category.values().length
                : pouch.getMatchingSlots(selectedCategory) + 1;
        if (nonEmptySlots > NUM_ROWS) {
            int scrollBarX = guiLeft + OFFSET_X + ITEM_POUCH_AREA_WIDTH;
            int scrollBarY = guiTop + OFFSET_Y;
            drawTexturedModalRect(scrollBarX, scrollBarY, SCROLL_BAR_X_BACKGROUND, SCROLL_BAR_Y,
                    SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT);
            int scrollBarYForeground = scrollBarY + SCROLL_BAR_HEIGHT * pouchSlotOffset / nonEmptySlots;
            int scrollBarHeight = Math.min(SCROLL_BAR_HEIGHT * NUM_ROWS / nonEmptySlots + 1,
                    scrollBarY + SCROLL_BAR_HEIGHT - scrollBarYForeground);
            drawTexturedModalRect(scrollBarX, scrollBarYForeground, SCROLL_BAR_X_FOREGROUND, SCROLL_BAR_Y,
                    SCROLL_BAR_WIDTH, scrollBarHeight);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (selectedCategory != null) {
            ItemStack toolTipStack = null;
            for (int row = 0; row < NUM_ROWS; row++) {
                int slotIndex = pouch.getSlotIndexForCategory(pouchSlotOffset + row, selectedCategory).orElse(Integer.MAX_VALUE);
                if (slotIndex < pouch.getSlots()) {
                    int x = guiLeft + OFFSET_X;
                    int y = guiTop + OFFSET_Y + row * 18;
                    ItemStack stack = pouch.getStackInSlot(slotIndex);
                    this.itemRender.renderItemIntoGUI(stack, x, y);
                    int textX = x + 19;
                    int textY = y + 5;
                    drawString(this.fontRenderer, stack.getDisplayName()
                                    + I18n.format("gui.pixelmonitempouch.amount", stack.getCount()),
                            textX, textY, 0xffffff);
                    if (mouseX >= x && mouseX < x + ITEM_POUCH_AREA_WIDTH && mouseY >= y && mouseY < y + 18) {
                        toolTipStack = stack;
                    }
                } else break;
            }
            if (toolTipStack != null) renderToolTip(toolTipStack, mouseX, mouseY);
            else renderHoveredToolTip(mouseX, mouseY);
        }
        else {
            for (int row = 0; row < NUM_ROWS; row++) {
                int categoryIndex = row + pouchSlotOffset;
                if (categoryIndex < Category.values().length) {
                    Category category = Category.values()[categoryIndex];
                    int x = guiLeft + OFFSET_X;
                    int y = guiTop + OFFSET_Y + row * 18;
                    this.itemRender.renderItemIntoGUI(category.getIcon(), x, y);
                    int textX = x + 19;
                    int textY = y + 5;
                    drawString(this.fontRenderer, I18n.format("gui.pixelmonitempouch.category."
                            + category.name().toLowerCase()), textX, textY, 0xffffff);
                } else break;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int newOffset = pouchSlotOffset - dWheel / Math.abs(dWheel);
            if (selectedCategory != null) {
                Optional<Integer> slotIndex = pouch.getSlotIndexForCategory(newOffset, selectedCategory);
                if (newOffset >= 0 && slotIndex.orElse(Integer.MAX_VALUE)
                        <= pouch.getMatchingSlots(selectedCategory) - NUM_ROWS + 1)
                    pouchSlotOffset = newOffset;
                else if (pouch.getSlotIndexForCategory(pouchSlotOffset, selectedCategory).orElse(Integer.MIN_VALUE)
                        > pouch.getMatchingSlots(selectedCategory) - NUM_ROWS + 1) {
                    pouchSlotOffset = 0;
                }
            } else {
                if (newOffset >= 0 && newOffset <= Category.values().length - NUM_ROWS + 1)
                    pouchSlotOffset = newOffset;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (int i = 0; i < NUM_ROWS; i++) {
            if (mouseX >= guiLeft + OFFSET_X && mouseX < guiLeft + OFFSET_X + ITEM_POUCH_AREA_WIDTH
                    && mouseY >= guiTop + OFFSET_Y + i * 18 && mouseY < guiTop + OFFSET_Y + i * 18 + 18) {
                if (selectedCategory != null) {
                    Optional<Integer> slotIndex = pouch.getSlotIndexForCategory(pouchSlotOffset + i, selectedCategory);
                    if (!slotIndex.isPresent()) {
                        Optional<Integer> emptySlot = pouch.getFirstEmptySlot();
                        Slot slot = inventorySlots.getSlot(ItemPouchContainer.PLAYER_INVENTORY_SLOT_COUNT
                                + emptySlot.orElse(pouch.getSlots()));
                        ClickType clickType = isShiftKeyDown() ? ClickType.QUICK_MOVE : ClickType.PICKUP;
                        this.handleMouseClick(slot, slot.slotNumber, mouseButton, clickType);
                    } else {
                        Slot slot = inventorySlots.getSlot(ItemPouchContainer.PLAYER_INVENTORY_SLOT_COUNT
                                + slotIndex.get());
                        ClickType clickType = isShiftKeyDown() ? ClickType.QUICK_MOVE : ClickType.PICKUP;
                        this.handleMouseClick(slot, slot.slotNumber, mouseButton, clickType);
                    }
                } else {
                    int categoryIndex = pouchSlotOffset + i;
                    if (categoryIndex < Category.values().length) {
                        selectedCategory = Category.values()[categoryIndex];
                        pouchSlotOffset = 0;
                    }
                }
                break;
            }
        }
    }
}
