package de.crazypokemondev.pixelmongenerations.pouch.common.items;

import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class BaseItem extends Item {
    public BaseItem(String itemName) {
        super();
        setRegistryName(PixelmonItemPouchMod.MOD_ID, itemName);
        setTranslationKey(PixelmonItemPouchMod.MOD_ID + "." + itemName);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }
}
