package net.samtrion.compactdrawers.core;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;

public final class ModConfig {
	public static final double StorageFactorCompactDrawer2By1 = 1.5;
	public static final double StorageFactorCompactDrawer2By1Half = .75;
	public static final double StorageFactorCompactDrawerHalf = .5;
	public static final int BaseStorageCompactDrawer;
	

	static {
		int baseCompDrawers = StorageDrawers.config.getBlockBaseStorage("compdrawers");
		if (baseCompDrawers <= 0) {
			baseCompDrawers = 16;
		}
		BaseStorageCompactDrawer = baseCompDrawers;
	}
}