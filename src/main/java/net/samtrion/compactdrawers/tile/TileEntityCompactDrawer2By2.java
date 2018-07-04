package net.samtrion.compactdrawers.tile;

import net.samtrion.compactdrawers.core.ModConfig;

public class TileEntityCompactDrawer2By2 extends TileEntityCompactDrawerBase {

    public TileEntityCompactDrawer2By2() {
        super(4, "compactdrawers.container.compactdrawer2by2", ModConfig.drawer2By2.capacity);
    }
}