package net.samtrion.compactdrawers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

@Mod(modid = CompactDrawers.MOD_ID, name = CompactDrawers.MOD_NAME, version = CompactDrawers.MOD_VERSION, dependencies = "required-after:storagedrawers;required-after:chameleon;after:waila;", acceptedMinecraftVersions = "[1.12,1.13)")
public class CompactDrawers {
	public static final String MOD_ID = "compactdrawers";
	public static final String MOD_NAME = "CompactDrawers";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String SOURCE_PATH = "net.samtrion.compactdrawers.";
	public static Logger log;

	@Mod.Instance(MOD_ID)
	public static CompactDrawers instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
	}
}
