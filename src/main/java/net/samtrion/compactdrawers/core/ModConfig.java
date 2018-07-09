package net.samtrion.compactdrawers.core;

import net.minecraftforge.common.config.*;
import net.samtrion.compactdrawers.CompactDrawers;

@Config(modid = CompactDrawers.MOD_ID, category = "block")
public final class ModConfig {

    public static ConfigDrawer drawerHalf     = new ConfigDrawer(true, 8);

    public static ConfigDrawer drawer2By1     = new ConfigDrawer(true, 24);

    public static ConfigDrawer drawer2By1Half = new ConfigDrawer(true, 12);

    public static ConfigDrawer drawer2By2     = new ConfigDrawer(true, 12);

    public static ConfigDrawer drawer2By2Half = new ConfigDrawer(true, 6);
}