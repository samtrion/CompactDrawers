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

public class TileEntityCompactDrawer2By1 extends TileEntityDrawers {
	@CapabilityInject(IDrawerAttributes.class)
	static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

	private GroupData groupData;

	private int capacity = 0;

	public TileEntityCompactDrawer2By1() {
		groupData = new GroupData(2);
		groupData.setCapabilityProvider(this);

		injectPortableData(groupData);
	}

	@Override
	public String getName() {
		return "compactdrawers.container.compactdrawer2by1";
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

		if (capacity == 0) {
			ConfigManager config = StorageDrawers.config;
			capacity = config.getBlockBaseStorage("compdrawers");

			if (capacity <= 0) {
				capacity = 24;
			} else {
				capacity *= 1.5;
			}
		}

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

		Minecraft.getMinecraft().addScheduledTask(() -> TileEntityCompactDrawer2By1.this.clientUpdateCountAsync(count));
	}

	@SideOnly(Side.CLIENT)
	private void clientUpdateCountAsync(int count) {
		groupData.setPooledCount(count);
	}

	private class GroupData extends FractionalDrawerGroup {
		public GroupData(int slotCount) {
			super(slotCount);
		}

		@Override
		protected World getWorld() {
			return TileEntityCompactDrawer2By1.this.getWorld();
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
				return (T) TileEntityCompactDrawer2By1.this.getDrawerAttributes();

			return super.getCapability(capability, facing);
		}
	}
}
