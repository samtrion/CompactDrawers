package net.samtrion.compactdrawers.tile;

import net.samtrion.compactdrawers.core.ModConfig;

public class TileEntityCompactDrawerHalf extends TileEntityCompactDrawerBase {

    public TileEntityCompactDrawerHalf() {
        super(3, "compactdrawers.container.compactdrawerhalf", ModConfig.drawerHalf.capacity);
    }
}
