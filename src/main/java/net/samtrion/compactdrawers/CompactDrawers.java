package net.samtrion.compactdrawers;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = CompactDrawers.MOD_ID, name = CompactDrawers.MOD_NAME, version = CompactDrawers.MOD_VERSION, dependencies = CompactDrawers.MOD_DEPENDENCIES, acceptedMinecraftVersions = CompactDrawers.MOD_MC_VERSIONS)
public class CompactDrawers {
    public static final String   MOD_ID           = "compactdrawers";
    public static final String   MOD_NAME         = "Compact Drawers";
    public static final String   MOD_VERSION      = "@VERSION@";
    public static final String   MOD_DEPENDENCIES = "required-after:forge;required-after:storagedrawers;required-after:chameleon;";
    public static final String   MOD_MC_VERSIONS  = "[1.12.2,1.13)";

    @Mod.Instance(MOD_ID)
    public static CompactDrawers instance;
}