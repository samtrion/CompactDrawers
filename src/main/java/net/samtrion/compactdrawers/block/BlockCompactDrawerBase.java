package net.samtrion.compactdrawers.block;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.INetworked;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.dynamic.StatusModelData;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.samtrion.compactdrawers.CompactDrawers;

public abstract class BlockCompactDrawerBase extends BlockDrawers implements INetworked {

	public static PropertyEnum SLOTS;

	private final int drawerCount;
	private final String registryName;
	
    @SideOnly(Side.CLIENT)
    private StatusModelData statusInfo;

	protected BlockCompactDrawerBase(String registryName, String blockName, int drawerCount) {
		super(Material.ROCK, registryName, checkBlockName(blockName));
		setSoundType(SoundType.STONE);
		this.registryName = registryName;
		this.drawerCount = drawerCount;
	}

	private static String checkBlockName(String blockName) {
		return (blockName.startsWith(CompactDrawers.MOD_ID + ".") ? blockName : CompactDrawers.MOD_ID + "." + blockName).toLowerCase();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public int getDrawerCount(IBlockState state) {
		return this.drawerCount;
	}

	@Override
	public boolean isHalfDepth(IBlockState state) {
		return false;
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, 0));
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void initDynamic () {
        ResourceLocation location = new ResourceLocation(CompactDrawers.MOD_ID + ":models/dynamic/" + this.registryName + ".json");
        statusInfo = new StatusModelData(this.drawerCount, location);
    }
	
	@Override
    public StatusModelData getStatusInfo (IBlockState state) {
        return statusInfo;
    }
}
