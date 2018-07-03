package net.samtrion.compactdrawers.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.samtrion.compactdrawers.tile.data.GroupData;

public abstract class TileEntityCompactDrawerBase extends TileEntityDrawers {
    private final int                    capacity;
    private final GroupData              groupData;
    private final int                    drawerCount;
    private final String                 name;

    protected TileEntityCompactDrawerBase(int drawerCount, String name, int drawerCapacity) {
        this.drawerCount = drawerCount;
        this.name = name;
        this.capacity = drawerCapacity;

        groupData = new GroupData(this.drawerCount, this);
        groupData.setCapabilityProvider(this);
        injectPortableData(groupData);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IDrawerGroup getGroup() {
        return groupData;
    }

    @Override
    public int getDrawerCapacity() {
        if (getWorld() == null || getWorld().isRemote)
            return super.getDrawerCapacity();
        return capacity;
    }

    @Override
    public boolean dataPacketRequiresRenderUpdate() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientUpdateCount(final int slot, final int count) {
        if (!getWorld().isRemote)
            return;

        Minecraft.getMinecraft().addScheduledTask(() -> this.clientUpdateCountAsync(count));
    }

    @SideOnly(Side.CLIENT)
    private void clientUpdateCountAsync(int count) {
        groupData.setPooledCount(count);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == DRAWER_GROUP_CAPABILITY)
            return (T) getGroup();

        if (getGroup().hasCapability(capability, facing))
            return getGroup().getCapability(capability, facing);

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == DRAWER_GROUP_CAPABILITY)
            return true;

        if (getGroup().hasCapability(capability, facing))
            return true;

        return super.hasCapability(capability, facing);
    }
}