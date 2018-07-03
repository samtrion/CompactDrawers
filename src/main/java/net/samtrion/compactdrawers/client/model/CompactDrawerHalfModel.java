package net.samtrion.compactdrawers.client.model;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.chameleon.resources.register.DefaultRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.samtrion.compactdrawers.block.BlockCompactDrawerBase;
import net.samtrion.compactdrawers.block.BlockCompactDrawerHalf;
import net.samtrion.compactdrawers.block.EnumCompactDrawerHalf;
import net.samtrion.compactdrawers.core.ModBlocks;

public final class CompactDrawerHalfModel {
    @SuppressWarnings("rawtypes")
    public static class Register extends DefaultRegister {

        @SuppressWarnings("unchecked")
        public Register() {
            super(ModBlocks.compactDrawerHalf);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<IBlockState> getBlockStates() {
            List<IBlockState> states = new ArrayList<>();

            for (EnumCompactDrawerHalf drawer : EnumCompactDrawerHalf.values()) {
                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                    states.add(getBlock().getDefaultState().withProperty(BlockCompactDrawerHalf.SLOTS, drawer).withProperty(BlockCompactDrawerBase.FACING, dir));
                }
            }
            return states;
        }

        @SuppressWarnings("unchecked")
        @Override
        public IBakedModel getModel(IBlockState state, IBakedModel existingModel) {
            return new CompactDrawerModel(existingModel, itemHandler, BlockCompactDrawerHalf.SLOTS);
        }

        @SuppressWarnings("unchecked")
        @Override
        public IBakedModel getModel(ItemStack stack, IBakedModel existingModel) {
            return new CompactDrawerModel(existingModel, itemHandler, BlockCompactDrawerHalf.SLOTS);
        }
    }

    private static final ItemHandler itemHandler = new ItemHandler();
}