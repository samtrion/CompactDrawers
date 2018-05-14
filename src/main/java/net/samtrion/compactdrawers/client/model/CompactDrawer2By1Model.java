package net.samtrion.compactdrawers.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jaquadro.minecraft.chameleon.model.PassLimitedModel;
import com.jaquadro.minecraft.chameleon.model.ProxyBuilderModel;
import com.jaquadro.minecraft.chameleon.resources.register.DefaultRegister;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.modeldata.DrawerStateModelData;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerDecoratorModel;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerSealedModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.util.Constants;
import net.samtrion.compactdrawers.block.BlockCompactDrawer2By1;
import net.samtrion.compactdrawers.block.EnumCompactDrawer2By1;
import net.samtrion.compactdrawers.core.ModBlocks;

public final class CompactDrawer2By1Model {
	public static class Register extends DefaultRegister {
		public Register() {
			super(ModBlocks.compactDrawer2By1);
		}

		@Override
		public List<IBlockState> getBlockStates() {
			List<IBlockState> states = new ArrayList<>();

			for (EnumCompactDrawer2By1 drawer : EnumCompactDrawer2By1.values()) {
				for (EnumFacing dir : EnumFacing.HORIZONTALS) {
					states.add(ModBlocks.compactDrawer2By1.getDefaultState()
							.withProperty(BlockCompactDrawer2By1.SLOTS, drawer)
							.withProperty(BlockCompactDrawer2By1.FACING, dir));
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

		@Override
		protected IBakedModel buildModel(IBlockState state, IBakedModel parent) {
			try {
				EnumCompactDrawer2By1 drawer = (EnumCompactDrawer2By1) state.getValue(BlockCompactDrawer2By1.SLOTS);
				EnumFacing dir = state.getValue(BlockCompactDrawer2By1.FACING);

				if (!(state instanceof IExtendedBlockState))
					return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);

				IExtendedBlockState xstate = (IExtendedBlockState) state;
				DrawerStateModelData stateModel = xstate.getValue(BlockDrawers.STATE_MODEL);

				if (!DrawerDecoratorModel.shouldHandleState(stateModel))
					return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);

				return new DrawerDecoratorModel(parent, xstate, drawer, dir, stateModel);
			} catch (Throwable t) {
				return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
			}
		}

		@Override
		public ItemOverrideList getOverrides() {
			return itemHandler;
		}
	}

	private static class ItemHandler extends ItemOverrideList {
		public ItemHandler() {
			super(ImmutableList.<ItemOverride>of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel parent, @Nonnull ItemStack stack, World world,
				EntityLivingBase entity) {
			if (stack.isEmpty())
				return parent;

			if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("tile", Constants.NBT.TAG_COMPOUND))
				return parent;

			Block block = Block.getBlockFromItem(stack.getItem());
			IBlockState state = block.getStateFromMeta(stack.getMetadata());

			return new DrawerSealedModel(parent, state, true);
		}
	}

	private static final ItemHandler itemHandler = new ItemHandler();
}