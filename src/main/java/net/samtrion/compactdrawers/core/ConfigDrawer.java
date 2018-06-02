package net.samtrion.compactdrawers.core;

import net.minecraftforge.common.config.Config.RangeInt;

public final class ConfigDrawer {
	public boolean enabled;
	
	@RangeInt(min = 1)
	public int capacity;

	public ConfigDrawer(boolean enabled, int drawerCapacity) {
		this.enabled = enabled;
		this.capacity = drawerCapacity;
	}

}