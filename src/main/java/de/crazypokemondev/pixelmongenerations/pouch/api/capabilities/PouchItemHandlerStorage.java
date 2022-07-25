package de.crazypokemondev.pixelmongenerations.pouch.api.capabilities;

import de.crazypokemondev.pixelmongenerations.pouch.common.capabilities.PouchItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

public class PouchItemHandlerStorage implements Capability.IStorage<IPouchItemHandler> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPouchItemHandler> capability, IPouchItemHandler instance, EnumFacing side) {
        if (instance instanceof PouchItemHandler) {
            return ((PouchItemHandler)instance).serializeNBT();
        }
        return null;
    }

    @Override
    public void readNBT(Capability<IPouchItemHandler> capability, IPouchItemHandler instance, EnumFacing side, NBTBase nbt) {
        if (instance instanceof PouchItemHandler && nbt instanceof NBTTagCompound) {
            ((PouchItemHandler)instance).deserializeNBT((NBTTagCompound) nbt);
        }
    }
}
