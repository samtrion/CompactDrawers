package net.samtrion.compactdrawers.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import com.jaquadro.minecraft.chameleon.resources.IItemVariantProvider;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.samtrion.compactdrawers.CompactDrawers;
import net.samtrion.compactdrawers.block.BlockCompactDrawerBase;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1;

public class ItemCompactDrawer extends ItemBlock implements IItemMeshMapper, IItemVariantProvider {
	private final BlockCompactDrawerBase block;

	public ItemCompactDrawer(BlockCompactDrawerBase block) {
		super(block);
		this.block = block;
	}

	@Override
	public List<ResourceLocation> getItemVariants() {
		ResourceLocation location = ForgeRegistries.ITEMS.getKey(this);
		List<ResourceLocation> variants = new ArrayList<ResourceLocation>();

		for (EnumCompactDrawer2By1 type : EnumCompactDrawer2By1.values()) {
			variants.add(new ResourceLocation(location.getResourceDomain(),
					location.getResourcePath() + '_' + type.getName()));
		}

		return variants;
	}

	@Override
	public List<Pair<ItemStack, ModelResourceLocation>> getMeshMappings() {
		List<Pair<ItemStack, ModelResourceLocation>> mappings = new ArrayList<Pair<ItemStack, ModelResourceLocation>>();

		for (EnumCompactDrawer2By1 type : EnumCompactDrawer2By1.values()) {
			ModelResourceLocation location = new ModelResourceLocation(
					CompactDrawers.MOD_ID + ":" + this.block.getName() + "_" + type.getName(), "inventory");
			mappings.add(Pair.of(new ItemStack(this, 1, type.getMetadata()), location));
		}
		return mappings;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
			return false;

		TileEntityDrawers tile = (TileEntityDrawers) world.getTileEntity(pos);
		if (tile != null) {
			if (side != EnumFacing.UP && side != EnumFacing.DOWN)
				tile.setDirection(side.ordinal());

			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tile"))
				tile.readFromPortableNBT(stack.getTagCompound().getCompoundTag("tile"));

			tile.setIsSealed(false);
		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack itemStack, @Nullable World world, List<String> list,
			ITooltipFlag advanced) {
		int count = this.block.getDrawerBaseStorage();
		list.add(I18n.format("storagedrawers.drawers.description", count));

		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("tile")) {
			list.add(ChatFormatting.YELLOW + I18n.format("storagedrawers.drawers.sealed"));
		}
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		createStackWithNBT(stack);
	}

	public static ItemStack createStackWithNBT(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();

		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		return stack;
	}
}