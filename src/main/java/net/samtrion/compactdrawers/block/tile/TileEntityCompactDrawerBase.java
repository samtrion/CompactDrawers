package net.samtrion.compactdrawers.block.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;
import com.jaquadro.minecraft.storagedrawers.network.CountUpdateMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.samtrion.compactdrawers.core.ModConfig;

public abstract class TileEntityCompactDrawerBase extends TileEntityDrawers {
	@CapabilityInject(IDrawerAttributes.class)
	static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

	private final int capacity;
	private final GroupData groupData;
	private final int drawerCount;
	private final String name;

	protected TileEntityCompactDrawerBase(int drawerCount, String name, double factor) {
		this.drawerCount = drawerCount;
		this.name = name;
		this.capacity = calcCapacity(factor);

		groupData = new GroupData(this.drawerCount, this);
		groupData.setCapabilityProvider(this);
		injectPortableData(groupData);
	}

	private int calcCapacity(double factor) {
		return (int) (ModConfig.BaseStorageCompactDrawer * factor);
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

	private class GroupData extends FractionalDrawerGroup {
		private final TileEntityDrawers entity;

		public GroupData(int slotCount, TileEntityDrawers entity) {
			super(slotCount);
			this.entity = entity;
		}

		@Override
		protected World getWorld() {
			return this.entity.getWorld();
		}

		@Override
		protected void log(String message) {
			if (!getWorld().isRemote && StorageDrawers.config.cache.debugTrace)
				StorageDrawers.log.info(message);
		}

		@Override
		protected int getStackCapacity() {
			return upgrades().getStorageMultiplier() * getEffectiveDrawerCapacity();
		}

		@Override
		protected void onItemChanged() {
			if (getWorld() != null && !getWorld().isRemote) {
				markDirty();
				markBlockForUpdate();
			}
		}

		@Override
		protected void onAmountChanged() {
			if (getWorld() != null && !getWorld().isRemote) {
				IMessage message = new CountUpdateMessage(getPos(), 0, getPooledCount());
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(
						getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 500);

				StorageDrawers.network.sendToAllAround(message, targetPoint);

				markDirty();
			}
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == TileEntityCompactDrawer2By1.DRAWER_ATTRIBUTES_CAPABILITY
					|| super.hasCapability(capability, facing);

		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if (capability == TileEntityCompactDrawer2By1.DRAWER_ATTRIBUTES_CAPABILITY)
				return (T) this.entity.getDrawerAttributes();

			return super.getCapability(capability, facing);
		}
	}
}