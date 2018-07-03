package net.samtrion.compactdrawers.client.model;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.chameleon.model.PassLimitedModel;
import com.jaquadro.minecraft.chameleon.model.ProxyBuilderModel;
import com.jaquadro.minecraft.chameleon.resources.register.DefaultRegister;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGeometry;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.modeldata.DrawerStateModelData;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerDecoratorModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.samtrion.compactdrawers.block.BlockCompactDrawer2By1;
import net.samtrion.compactdrawers.block.BlockCompactDrawerBase;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1;
import net.samtrion.compactdrawers.core.ModBlocks;

public final class CompactDrawer2By1Model {
    @SuppressWarnings("rawtypes")
    public static class Register extends DefaultRegister {

        @SuppressWarnings("unchecked")
        public Register() {
            super(ModBlocks.compactDrawer2By1);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<IBlockState> getBlockStates() {
            List<IBlockState> states = new ArrayList<>();

            for (EnumCompactDrawer2By1 drawer : EnumCompactDrawer2By1.values()) {
                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                    states.add(getBlock().getDefaultState().withProperty(BlockCompactDrawer2By1.SLOTS, drawer).withProperty(BlockCompactDrawerBase.FACING, dir));
                }
            }
            return states;
        }

        @Override
        public IBakedModel getModel(IBlockState state, IBakedModel existingModel) {
            return new Model(existingModel);
        }

        @Override
        public IBakedModel getModel(ItemStack stack, IBakedModel existingModel) {
            return new Model(existingModel);
        }
    }

    public static class Model extends ProxyBuilderModel {
        public Model(IBakedModel parent) {
            super(parent);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected IBakedModel buildModel(IBlockState state, IBakedModel parent) {
            try {
                IDrawerGeometry drawer = (IDrawerGeometry) state.getValue(BlockCompactDrawer2By1.SLOTS);
                EnumFacing dir = state.getValue(BlockCompactDrawerBase.FACING);

                if (!(state instanceof IExtendedBlockState))
                    return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);

                IExtendedBlockState xstate = (IExtendedBlockState) state;
                DrawerStateModelData stateModel = xstate.getValue(BlockDrawers.STATE_MODEL);

                if (!DrawerDecoratorModel.shouldHandleState(stateModel))
                    return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);

                return new DrawerDecoratorModel(parent, xstate, drawer, dir, stateModel);
            }
            catch (Throwable t) {
                return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
            }
        }

        @Override
        public ItemOverrideList getOverrides() {
            return itemHandler;
        }
    }

    private static final ItemHandler itemHandler = new ItemHandler();
}