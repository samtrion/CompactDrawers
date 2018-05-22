package net.samtrion.compactdrawers.block;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.dynamic.StatusModelData;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.samtrion.compactdrawers.CompactDrawers;
import net.samtrion.compactdrawers.core.ModConfig;
import net.samtrion.compactdrawers.item.ItemCompactDrawer;

public abstract class BlockCompactDrawerBase extends BlockDrawers {

	private final String registryName;
	private final double factor;

	@SideOnly(Side.CLIENT)
	private StatusModelData statusInfo;

	protected BlockCompactDrawerBase(String registryName, String blockName, double factor) {
		super(Material.ROCK, registryName, checkBlockName(blockName));
		setSoundType(SoundType.STONE);
		this.registryName = registryName;
		this.factor = factor;
	}

	private static String checkBlockName(String blockName) {
		return (blockName.startsWith(CompactDrawers.MOD_ID + ".") ? blockName : CompactDrawers.MOD_ID + "." + blockName)
				.toLowerCase();
	}

	public String getName() {
		return this.registryName;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	public int getDrawerBaseStorage() {
		return (int) (ModConfig.BaseStorageCompactDrawer * this.factor);
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		list.add(ItemCompactDrawer.createStackWithNBT(new ItemStack(this, 1, 0)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initDynamic() {
		ResourceLocation location = new ResourceLocation(
				CompactDrawers.MOD_ID + ":models/dynamic/" + this.registryName + ".json");

		IBlockState state = this.getDefaultState();
		statusInfo = new StatusModelData(getDrawerCount(state), location);
	}

	@Override
	public StatusModelData getStatusInfo(IBlockState state) {
		return statusInfo;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return isOpaqueCube(state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return !isHalfDepth(state);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		if (isFullCube(state)) {
			return true;
		}
		TileEntityDrawers tile = getTileEntity(world, pos);
		return (tile != null && tile.getDirection() == face.getOpposite().getIndex());
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (isFullCube(state)) {
			return true;
		}
		TileEntityDrawers tile = getTileEntity(blockAccess, pos);
		if (tile != null && tile.getDirection() == side.getIndex()) {
			return true;
		}
		return super.shouldSideBeRendered(state, blockAccess, pos, side);
	}

	@Override
	protected int getDrawerSlot(int drawerCount, int side, float hitX, float hitY, float hitZ) {
		if (drawerCount == 2 && !hitTop(hitY)) {
			return 1;
		} else if (drawerCount == 3) {
			if (!hitTop(hitY)) {
				return hitLeft(side, hitX, hitZ) ? 1 : 2;
			}
		} else if (drawerCount == 4) {
			if (!hitTop(hitY)) {
				return hitLeft(side, hitX, hitZ) ? 2 : 3;
			} else if (!hitLeft(side, hitX, hitZ)) {
				return 1;
			}
		}
		return 0;
	}
}
