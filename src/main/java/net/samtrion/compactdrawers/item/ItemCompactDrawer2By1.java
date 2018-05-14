package net.samtrion.compactdrawers.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import com.jaquadro.minecraft.chameleon.resources.IItemVariantProvider;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.samtrion.compactdrawers.CompactDrawers;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1;

public class ItemCompactDrawer2By1 extends ItemCompactDrawerBase {

	public ItemCompactDrawer2By1(Block block) {
		super(block);
	}

	@Override
	public List<ResourceLocation> getItemVariants() {
		ResourceLocation location = ForgeRegistries.ITEMS.getKey(this);
		List<ResourceLocation> variants = new ArrayList<ResourceLocation>();

		for (EnumCompactDrawer2By1 type : EnumCompactDrawer2By1.values())
			variants.add(new ResourceLocation(location.getResourceDomain(),
					location.getResourcePath() + '_' + type.getName()));

		return variants;
	}

	@Override
	public List<Pair<ItemStack, ModelResourceLocation>> getMeshMappings() {
		List<Pair<ItemStack, ModelResourceLocation>> mappings = new ArrayList<Pair<ItemStack, ModelResourceLocation>>();

		for (EnumCompactDrawer2By1 type : EnumCompactDrawer2By1.values()) {
			ModelResourceLocation location = new ModelResourceLocation(
					CompactDrawers.MOD_ID + ":compact_drawer_2by1_" + type.getName(), "inventory");
			mappings.add(Pair.of(new ItemStack(this, 1, type.getMetadata()), location));
		}
		return mappings;
	}

	@Override
	protected int getBlockBaseStorage() {
		int result = StorageDrawers.config.getBlockBaseStorage("compdrawers");
		if (result <= 0)
			return 24;
		return (int) (result * 1.5);
	}
}