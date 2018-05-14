package net.samtrion.compactdrawers.core;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.resources.ModelRegistry;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.client.renderer.TileEntityDrawersRenderer;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.samtrion.compactdrawers.CompactDrawers;
import net.samtrion.compactdrawers.block.BlockCompactDrawer2By1;
import net.samtrion.compactdrawers.block.tile.TileEntityCompactDrawer2By1;
import net.samtrion.compactdrawers.client.model.CompactDrawer2By1Model;
import net.samtrion.compactdrawers.item.ItemCompactDrawer2By1;

public class ModBlocks {
	public static BlockCompactDrawer2By1 compactDrawer2By1;

	@ObjectHolder("storagedrawers:basicdrawers")
	public static final Block basicDrawer = null;
	@ObjectHolder("storagedrawersextra:extra_drawers")
	public static final Block extraDrawer = null;

	@Mod.EventBusSubscriber(modid = CompactDrawers.MOD_ID)
	public static class Registration {
		private static final ConfigManager config = StorageDrawers.config;

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			IForgeRegistry<Block> registry = event.getRegistry();
			if (config.isBlockEnabled("compdrawers")) {
				compactDrawer2By1 = new BlockCompactDrawer2By1("compact_drawer_2by1", "compactDrawer2By1");
				registry.registerAll(compactDrawer2By1);
				GameRegistry.registerTileEntity(TileEntityCompactDrawer2By1.class,
						CompactDrawers.MOD_ID + ":compact_drawer_2by1");
			}
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();
			if (config.isBlockEnabled("compdrawers")) {
				registry.register(new ItemCompactDrawer2By1(compactDrawer2By1)
						.setRegistryName(compactDrawer2By1.getRegistryName()));

				registerOreDictionary("drawer2Slots", EnumBasicDrawer.FULL2, EnumBasicDrawer.HALF2);
				// registerOreDictionary("drawer4Slots", EnumBasicDrawer.FULL4, EnumBasicDrawer.HALF4);
			}

		}

		private static void registerOreDictionary(String key, EnumBasicDrawer... types) {
			for (EnumBasicDrawer type : types) {
				OreDictionary.registerOre(key, new ItemStack(basicDrawer, 1, type.getMetadata()));
				if (extraDrawer != null) {
					OreDictionary.registerOre(key, new ItemStack(extraDrawer, 1, type.getMetadata()));
				}
			}
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void registerModels(ModelRegistryEvent event) {
			if (config.isBlockEnabled("compdrawers")) {
				compactDrawer2By1.initDynamic();

				ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;
				modelRegistry.registerModel(new CompactDrawer2By1Model.Register());

				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactDrawer2By1.class,
						new TileEntityDrawersRenderer());
			}
		}
	}
}