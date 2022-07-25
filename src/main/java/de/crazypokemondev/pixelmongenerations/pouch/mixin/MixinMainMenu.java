package de.crazypokemondev.pixelmongenerations.pouch.mixin;

import com.pixelmongenerations.client.gui.battles.GuiBattle;
import com.pixelmongenerations.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmongenerations.client.gui.battles.battleScreens.MainMenu;
import com.pixelmongenerations.core.Pixelmon;
import com.pixelmongenerations.core.enums.battle.BattleMode;
import com.pixelmongenerations.core.network.PixelmonData;
import com.pixelmongenerations.core.network.packetHandlers.battles.BagPacket;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.Capabilities;
import de.crazypokemondev.pixelmongenerations.pouch.api.capabilities.IPouchItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Objects;

@Mixin(MainMenu.class)
public abstract class MixinMainMenu extends BattleScreen {
    @Shadow(remap = false)
    public static Integer lastId;
    private boolean alreadySentPacket;

    public MixinMainMenu(GuiBattle parent, BattleMode mode) {
        super(parent, mode);
    }

    @SuppressWarnings({"rawtypes", "InvalidInjectorMethodSignature", "UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = "drawScreen", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onDrawScreen(int width, int height, int mouseX, int mouseY, CallbackInfo ci, PixelmonData data, int guiHeight, int guiWidth, Item item, int bX, int bY, int bWidth, int bHeight, Iterator var13, ItemStack i) {
        if (i.hasCapability(Capabilities.ITEM_POUCH, null)) {
            IPouchItemHandler pouch = Objects.requireNonNull(i.getCapability(Capabilities.ITEM_POUCH, null));
            for (int i1 = 0; i1 < pouch.getSlots(); i1++) {
                ItemStack stack = pouch.getStackInSlot(i1);
                if (Item.getIdFromItem(stack.getItem()) == lastId) {
                    if (mouseX > bX && mouseX < bX + bWidth && mouseY > bY && mouseY < bY + bHeight) {
                        GL11.glColor3f(0.95F, 0.95F, 0.95F);
                    } else {
                        GL11.glColor3f(0.8F, 0.8F, 0.8F);
                    }
                    break;
                }
            }
        }
    }

    @SuppressWarnings({"UnresolvedMixinReference", "InvalidInjectorMethodSignature", "MixinAnnotationTarget", "rawtypes"})
    @Inject(method = "click", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onClick(int width, int height, int mouseX, int mouseY, CallbackInfo ci, int guiHeight, int guiWidth, int bX, int bY, int bWidth, int bHeight, Iterator var11, ItemStack i) {
        if (i.hasCapability(Capabilities.ITEM_POUCH, null)) {
            IPouchItemHandler pouch = Objects.requireNonNull(i.getCapability(Capabilities.ITEM_POUCH, null));
            for (int i1 = 0; i1 < pouch.getSlots(); i1++) {
                ItemStack stack = pouch.getStackInSlot(i1);
                if (Item.getIdFromItem(stack.getItem()) == lastId) {
                    Pixelmon.NETWORK.sendToServer(new BagPacket(this.bm.getUserPokemonPacket().pokemonID, lastId, this.bm.battleControllerIndex, 0));
                    this.bm.mode = BattleMode.Waiting;
                    this.alreadySentPacket = true;
                    break;
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Redirect(method = "click", remap = false, at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", remap = false))
    private boolean onHasNext(Iterator iterator) {
        return !this.alreadySentPacket && iterator.hasNext();
    }

    @Inject(method = "click", remap = false, at = @At("RETURN"))
    private void onClickReturn(int width, int height, int mouseX, int mouseY, CallbackInfo ci) {
        this.alreadySentPacket = false;
    }
}
