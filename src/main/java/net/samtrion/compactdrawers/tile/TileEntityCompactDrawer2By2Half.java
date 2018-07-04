package net.samtrion.compactdrawers.tile;

import net.samtrion.compactdrawers.core.ModConfig;

public class TileEntityCompactDrawer2By2Half extends TileEntityCompactDrawerBase {

    public TileEntityCompactDrawer2By2Half() {
        super(4, "compactdrawers.container.compactdrawer2by2half", ModConfig.drawer2By2Half.capacity);
    }
}