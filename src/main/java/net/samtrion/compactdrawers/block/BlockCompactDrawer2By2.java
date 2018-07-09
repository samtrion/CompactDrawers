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
import net.samtrion.compactdrawers.core.ModConfig;
import net.samtrion.compactdrawers.tile.TileEntityCompactDrawer2By2;

public class BlockCompactDrawer2By2 extends BlockCompactDrawerBase {
    public static PropertyEnum<EnumCompactDrawer2By2> SLOTS = PropertyEnum.create("slots", EnumCompactDrawer2By2.class);

    public BlockCompactDrawer2By2(String registryName, String blockName) {
        super(registryName, blockName, ModConfig.drawer2By2.capacity);
    }

    @Override
    protected void initDefaultState() {
        super.initDefaultState();
        setDefaultState(blockState.getBaseState().withProperty(SLOTS, EnumCompactDrawer2By2.L2R2).withProperty(FACING, EnumFacing.NORTH));
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

        EnumCompactDrawer2By2 slots = EnumCompactDrawer2By2.L2R2;
        boolean emptyTopLeft = tile.getDrawer(0).isEmpty();
        boolean emptyBottomLeft = tile.getDrawer(1).isEmpty();
        boolean emptyTopRight = tile.getDrawer(2).isEmpty();
        boolean emptyBottomRight = tile.getDrawer(3).isEmpty();

        if (emptyTopLeft != emptyBottomLeft) {
            if (emptyTopRight == emptyBottomRight) {
                slots = EnumCompactDrawer2By2.L1R2;
            } else {
                slots = EnumCompactDrawer2By2.L1R1;
            }
        } else if (emptyTopRight != emptyBottomRight) {
            if (emptyTopLeft == emptyBottomLeft) {
                slots = EnumCompactDrawer2By2.L2R1;
            } else {
                slots = EnumCompactDrawer2By2.L1R1;
            }
        }

        return super.getActualState(state, world, pos).withProperty(SLOTS, slots);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCompactDrawer2By2();
    }
}