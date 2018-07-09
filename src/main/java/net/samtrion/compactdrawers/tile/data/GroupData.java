package net.samtrion.compactdrawers.tile.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.network.CountUpdateMessage;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawerBase;

public final class GroupData extends FractionalDrawerGroup {

    @CapabilityInject(IDrawerAttributes.class)
    public static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

    private final TileEntityCompactDrawerBase   entity;

    public GroupData(int slotCount, TileEntityCompactDrawerBase entity) {
        super(slotCount);
        this.entity = entity;
    }

    @Override
    protected World getWorld() {
        return this.entity.getWorld();
    }

    @Override
    protected int getStackCapacity() {
        return this.entity.upgrades().getStorageMultiplier() * this.entity.getEffectiveDrawerCapacity();
    }

    @Override
    protected void onItemChanged() {
        if (getWorld() != null && !getWorld().isRemote) {
            this.entity.markDirty();
            this.entity.markBlockForUpdate();
        }
    }

    @Override
    protected void onAmountChanged() {
        if (getWorld() != null && !getWorld().isRemote) {
            BlockPos pos = this.entity.getPos();
            IMessage message = new CountUpdateMessage(pos, 0, getPooledCount());
            NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 500);

            StorageDrawers.network.sendToAllAround(message, targetPoint);

            this.entity.markDirty();
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == DRAWER_ATTRIBUTES_CAPABILITY || super.hasCapability(capability, facing);

    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == DRAWER_ATTRIBUTES_CAPABILITY)
            return (T) this.entity.getDrawerAttributes();

        return super.getCapability(capability, facing);
    }

}
