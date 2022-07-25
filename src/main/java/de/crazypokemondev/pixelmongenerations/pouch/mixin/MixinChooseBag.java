package de.crazypokemondev.pixelmongenerations.pouch.mixin;

import com.pixelmongenerations.client.gui.battles.GuiBattle;
import com.pixelmongenerations.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmongenerations.client.gui.battles.battleScreens.ChooseBag;
import com.pixelmongenerations.core.enums.battle.BagSection;
import com.pixelmongenerations.core.enums.battle.BattleMode;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChooseBag.class)
public abstract class MixinChooseBag extends BattleScreen {
    @Shadow(remap = false)
    public static boolean checkItem(BagSection section, Item item) {
        return false;
    }

    @Shadow(remap = false)
    protected abstract void checkExists(Item item, int count);

    public MixinChooseBag(GuiBattle parent, BattleMode mode) {
        super(parent, mode);
    }

    @Inject(method = "getInventory", at = @At("HEAD"), remap = false)
    private void onGetInventory(CallbackInfo ci) {
        for (ItemStack stack : this.mc.player.inventory.mainInventory) {
            if (stack.hasCapability(Capabilities.ITEM_POUCH, null)) {
                IPouchItemHandler pouch = stack.getCapability(Capabilities.ITEM_POUCH, null);
                if (pouch != null) {
                    for (int i = 0; i < pouch.getSlots(); i++) {
                        ItemStack innerStack = pouch.getStackInSlot(i);
                        if (!innerStack.isEmpty()) {
                            Item item = innerStack.getItem();
                            boolean valid = checkItem(this.bm.bagSection, item);
                            if (valid) {
                                this.checkExists(item, innerStack.getCount());
                            }
                        }
                    }
                }
            }
        }
    }
}
