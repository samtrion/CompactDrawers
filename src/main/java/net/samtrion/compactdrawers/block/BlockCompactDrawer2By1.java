package net.samtrion.compactdrawers.block;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawer2By1;
import net.samtrion.compactdrawers.core.ModConfig;

public class BlockCompactDrawer2By1 extends BlockCompactDrawerBase {
    public static PropertyEnum<EnumCompactDrawer2By1> SLOTS = PropertyEnum.create("slots", EnumCompactDrawer2By1.class);

    public BlockCompactDrawer2By1(String registryName, String blockName) {
        super(registryName, blockName, ModConfig.drawer2By1.capacity);
    }

    @Override
    protected void initDefaultState() {
        super.initDefaultState();
        setDefaultState(blockState.getBaseState().withProperty(SLOTS, EnumCompactDrawer2By1.OPEN1).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { SLOTS, FACING }, new IUnlistedProperty[] { STATE_MODEL });
    }

    @Override
    public int getDrawerCount(IBlockState state) {
        return getDrawerGeometry(state, SLOTS).getDrawerCount();
    }

    @Override
    public boolean isHalfDepth(IBlockState state) {
        return getDrawerGeometry(state, SLOTS).isHalfDepth();
    }

    @SuppressWarnings({ "deprecation" })
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityDrawers tile = getTileEntity(world, pos);
        if (tile == null) {
            return state;
        }

        EnumCompactDrawer2By1 slots;
        if (tile.getDrawer(1).isEnabled()) {
            slots = EnumCompactDrawer2By1.OPEN2;
        }
        else {
            slots = EnumCompactDrawer2By1.OPEN1;
        }

        return super.getActualState(state, world, pos).withProperty(SLOTS, slots);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCompactDrawer2By1();
    }
}
