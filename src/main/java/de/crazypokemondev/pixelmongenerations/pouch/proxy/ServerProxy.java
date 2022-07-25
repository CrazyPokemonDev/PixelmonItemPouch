package de.crazypokemondev.pixelmongenerations.pouch.proxy;

import de.crazypokemondev.pixelmongenerations.pouch.PixelmonItemPouchMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = PixelmonItemPouchMod.MOD_ID)
public class ServerProxy extends CommonProxy {

}
