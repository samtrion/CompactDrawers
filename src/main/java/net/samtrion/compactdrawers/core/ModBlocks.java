package net.samtrion.compactdrawers.core;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.resources.ModelRegistry;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.samtrion.compactdrawers.CompactDrawers;
import net.samtrion.compactdrawers.block.BlockCompactDrawer2By1;
import net.samtrion.compactdrawers.block.BlockCompactDrawer2By1Half;
import net.samtrion.compactdrawers.block.BlockCompactDrawerBase;
import net.samtrion.compactdrawers.block.BlockCompactDrawerHalf;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1Half;
import net.samtrion.compactdrawers.block.EnumCompactDrawerHalf;
import net.samtrion.compactdrawers.block.IDrawerSerializable;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawer2By1;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawer2By1Half;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawerHalf;
import net.samtrion.compactdrawers.client.model.CompactDrawer2By1HalfModel;
import net.samtrion.compactdrawers.client.model.CompactDrawer2By1Model;
import net.samtrion.compactdrawers.client.model.CompactDrawerHalfModel;
import net.samtrion.compactdrawers.client.renderer.TileEntityCompactDrawerRenderer;
import net.samtrion.compactdrawers.item.ItemCompactDrawer;

public class ModBlocks {
	@ObjectHolder(CompactDrawers.MOD_ID + ":compact_drawer_2by1")
	public static final BlockCompactDrawer2By1 compactDrawer2By1 = null;
	@ObjectHolder(CompactDrawers.MOD_ID + ":compact_drawer_2by1_half")
	public static final BlockCompactDrawer2By1Half compactDrawer2By1Half = null;
	@ObjectHolder(CompactDrawers.MOD_ID + ":compact_drawer_half")
	public static final BlockCompactDrawerHalf compactDrawerHalf = null;

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
				registry.register(new BlockCompactDrawer2By1("compact_drawer_2by1", "compactDrawer2By1"));
				GameRegistry.registerTileEntity(TileEntityCompactDrawer2By1.class,
						CompactDrawers.MOD_ID + ":compact_drawer_2by1");

				registry.register(new BlockCompactDrawer2By1Half("compact_drawer_2by1_half", "compactDrawer2By1Half"));
				GameRegistry.registerTileEntity(TileEntityCompactDrawer2By1Half.class,
						CompactDrawers.MOD_ID + ":compact_drawer_2by1_half");

				registry.register(new BlockCompactDrawerHalf("compact_drawer_half", "compactDrawerHalf"));
				GameRegistry.registerTileEntity(TileEntityCompactDrawerHalf.class,
						CompactDrawers.MOD_ID + ":compact_drawer_half");
			}
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();

			registerCompactDrawerItem(registry, EnumCompactDrawer2By1.values(), compactDrawer2By1);
			registerCompactDrawerItem(registry, EnumCompactDrawer2By1Half.values(), compactDrawer2By1Half);
			registerCompactDrawerItem(registry, EnumCompactDrawerHalf.values(), compactDrawerHalf);
		}

		private static void registerCompactDrawerItem(IForgeRegistry<Item> registry, IDrawerSerializable[] drawerValues, BlockCompactDrawerBase block) {
			if (block != null) {
				registry.register(new ItemCompactDrawer(block, drawerValues).setRegistryName(block.getRegistryName()));
			}
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void registerModels(ModelRegistryEvent event) {
			ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;

			if (compactDrawer2By1 != null) {
				compactDrawer2By1.initDynamic();
				modelRegistry.registerModel(new CompactDrawer2By1Model.Register());

				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactDrawer2By1.class,
						new TileEntityCompactDrawerRenderer());
			}

			if (compactDrawer2By1Half != null) {
				compactDrawer2By1Half.initDynamic();
				modelRegistry.registerModel(new CompactDrawer2By1HalfModel.Register());

				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactDrawer2By1Half.class,
						new TileEntityCompactDrawerRenderer());
			}

			if (compactDrawerHalf != null) {
				compactDrawerHalf.initDynamic();
				modelRegistry.registerModel(new CompactDrawerHalfModel.Register());

				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactDrawerHalf.class,
						new TileEntityCompactDrawerRenderer());
			}
		}

	}
}