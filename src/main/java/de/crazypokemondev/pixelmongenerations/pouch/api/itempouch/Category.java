package de.crazypokemondev.pixelmongenerations.pouch.api.itempouch;

import com.pixelmongenerations.common.item.*;
import com.pixelmongenerations.common.item.heldItems.ItemBerry;
import com.pixelmongenerations.common.item.heldItems.ZCrystal;
import com.pixelmongenerations.core.config.*;
import de.crazypokemondev.pixelmongenerations.pouch.common.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public enum Category {
    NONE(new ItemStack(ModItems.ITEM_POUCH)),
    MEDICINE(new ItemStack(PixelmonItems.potion)),
    POKE_BALLS(new ItemStack(PixelmonItemsPokeballs.pokeBall)),
    BATTLE_ITEMS(new ItemStack(PixelmonItems.xAttack)),
    BERRIES(new ItemStack(PixelmonItemsHeld.cheriBerry)),
    APRICORNS(new ItemStack(PixelmonItemsApricorns.apricornBlack)),
    OTHER_ITEMS(new ItemStack(PixelmonItems.rareCandy)),
    TMS(new ItemStack(PixelmonItemsTMs.TMs.get(0))),
    TREASURES(new ItemStack(PixelmonItems.pearl)),
    INGREDIENTS(new ItemStack(PixelmonItemsCurryIngredients.sausages)),
    Z_CRYSTALS(new ItemStack(PixelmonItemsHeld.buginumZ)),
    KEY_ITEMS(new ItemStack(PixelmonItems.redBike));

    private final ItemStack icon;

    Category(ItemStack icon) {
        this.icon = icon;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public boolean containsItem(Item item) {
        switch (this) {
            case MEDICINE:
                return item instanceof ItemMedicine;
            case POKE_BALLS:
                return item instanceof ItemPokeball;
            case BATTLE_ITEMS:
                return item instanceof ItemBattleItem;
            case BERRIES:
                return item instanceof ItemBerry;
            case APRICORNS:
                return item instanceof ItemApricorn || item instanceof ItemApricornCooked;
            case OTHER_ITEMS:
                return item != Items.AIR
                        && Arrays.stream(values()).noneMatch(x -> x != OTHER_ITEMS && x != NONE && x.containsItem(item));
            case TMS:
                return item instanceof ItemTM;
            case TREASURES:
                return Arrays.stream(treasures).anyMatch(x -> x == item);
            case INGREDIENTS:
                return item instanceof CurryIngredient;
            case Z_CRYSTALS:
                return item instanceof ZCrystal;
            case KEY_ITEMS:
                return Arrays.stream(keyItems).anyMatch(x -> x == item);
            case NONE:
            default:
                return item != Items.AIR;
        }
    }

    private static final Item[] treasures = new Item[] {
            PixelmonItems.cometShard,
            PixelmonItems.starPiece,
            PixelmonItems.yellowShard,
            PixelmonItems.greenShard,
            PixelmonItems.blueShard,
            PixelmonItems.redShard,
            PixelmonItems.bigMushroom,
            PixelmonItems.balmMushroom,
            PixelmonItems.tinyMushroom,
            PixelmonItems.nugget,
            PixelmonItems.bigNugget,
            PixelmonItems.pearl,
            PixelmonItems.bigPearl,
            PixelmonItems.pearlString,
    };

    private static final Item[] keyItems = new Item[] {
            PixelmonItems.orb,
            PixelmonItems.unoOrb,
            PixelmonItems.dosOrb,
            PixelmonItems.tresOrb,
            PixelmonItems.gift,
            PixelmonItems.cameraItem,
            PixelmonItems.filmItem,
            PixelmonItems.abilityCapsule,
            PixelmonItems.expAll,
            PixelmonItems.hourglassGold,
            PixelmonItems.hourglassSilver,
            PixelmonItems.meteorite,
            PixelmonItems.gracidea,
            PixelmonItems.reveal_glass,
            Item.getItemFromBlock(PixelmonBlocks.prisonBottle),
            PixelmonItems.redchain,
            PixelmonItems.dnaSplicers,
            PixelmonItemsHeld.adamant_orb,
            PixelmonItemsHeld.lustrous_orb,
            PixelmonItemsHeld.griseous_orb,
            PixelmonItems.legendFinder,
            PixelmonItems.fadedRedOrb,
            PixelmonItems.fadedBlueOrb,
            PixelmonItems.itemFinder,
            PixelmonItems.grottoSpawner,
            PixelmonItems.bikeFrame,
            PixelmonItems.bikeHandlebars,
            PixelmonItems.bikeSeat,
            PixelmonItems.bikeWheel,
            PixelmonItems.blueBike,
            PixelmonItems.greenBike,
            PixelmonItems.orangeBike,
            PixelmonItems.purpleBike,
            PixelmonItems.redBike,
            PixelmonItems.yellowBike,
            PixelmonItems.zygardeCube,
            PixelmonItems.meltanBox,
            PixelmonItems.sunFlute,
            PixelmonItems.moonFlute,
            PixelmonItems.pinkNectar,
            PixelmonItems.redNectar,
            PixelmonItems.purpleNectar,
            PixelmonItems.yellowNectar,
            PixelmonItems.nLunarizer,
            PixelmonItems.nSolarizer,
    };
}
