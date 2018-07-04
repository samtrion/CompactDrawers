package net.samtrion.compactdrawers.block;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGeometry;

public enum EnumCompactDrawer2By2 implements IDrawerGeometry, IDrawerSerializable {
    L2R2(0, 4, "l2r2"),
    L1R2(1, 2, "l1r2"),
    L0R2(2, 2, "l0r2"),
    L2R1(3, 2, "l2r1"),
    L2R0(4, 2, "l2r0"),
    L1R1(5, 2, "l1r1"),
    L0R1(6, 1, "l0r1"),
    L1R0(7, 1, "l1r0"),
    L0R0(8, 0, "l0r0");
    
    private static final EnumCompactDrawer2By2[] META_LOOKUP;

    private final int                            meta;
    private final int                            openSlots;
    private final String                         name;

    EnumCompactDrawer2By2(int meta, int openSlots, String name) {
        this.meta = meta;
        this.name = name;
        this.openSlots = openSlots;
    }

    static {
        META_LOOKUP = new EnumCompactDrawer2By2[values().length];
        for (EnumCompactDrawer2By2 type : values()) {
            META_LOOKUP[type.getMetadata()] = type;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDrawerCount() {
        return 4;
    }

    @Override
    public boolean isHalfDepth() {
        return false;
    }

    @Override
    public int getMetadata() {
        return meta;
    }

    public int getOpenSlots() {
        return openSlots;
    }
}
