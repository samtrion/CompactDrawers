package net.samtrion.compactdrawers.block;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGeometry;

import net.minecraft.util.IStringSerializable;

public enum EnumCompactDrawerHalf implements IDrawerGeometry, IStringSerializable
{
    OPEN1(0, 1, "open1"),
    OPEN2(1, 2, "open2"),
    OPEN3(2, 3, "open3");

    private static final EnumCompactDrawerHalf[] META_LOOKUP;

    private final int meta;
    private final int openSlots;
    private final String name;

    EnumCompactDrawerHalf (int meta, int openSlots, String name) {
        this.meta = meta;
        this.name = name;
        this.openSlots = openSlots;
    }

    @Override
    public boolean isHalfDepth () {
        return true;
    }

    @Override
    public int getDrawerCount () {
        return 3;
    }

    public int getMetadata () {
        return meta;
    }

    public int getOpenSlots () {
        return openSlots;
    }

    public static EnumCompactDrawerHalf byMetadata (int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length)
            meta = 0;
        return META_LOOKUP[meta];
    }

    @Override
    public String toString () {
        return getName();
    }

    @Override
    public String getName () {
        return name;
    }

    static {
        META_LOOKUP = new EnumCompactDrawerHalf[values().length];
        for (EnumCompactDrawerHalf upgrade : values()) {
            META_LOOKUP[upgrade.getMetadata()] = upgrade;
        }
    }
}