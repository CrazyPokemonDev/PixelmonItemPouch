package de.crazypokemondev.pixelmongenerations.pouch.client.gui;

import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import de.crazypokemondev.pixelmongenerations.pouch.common.gui.ItemPouchContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandler implements IGuiHandler {

    public static final int ITEM_POUCH = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ITEM_POUCH) {
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            IPouchItemHandler pouch = stack.getCapability(Capabilities.ITEM_POUCH, null);
            if (pouch != null) {
                return new ItemPouchContainer(pouch, player.inventory);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ITEM_POUCH) {
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            IPouchItemHandler pouch = stack.getCapability(Capabilities.ITEM_POUCH, null);
            if (pouch != null) {
                return new ItemPouchGuiContainer(new ItemPouchContainer(pouch, player.inventory), pouch);
            }
        }
        return null;
    }
}
