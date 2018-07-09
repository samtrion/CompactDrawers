package net.samtrion.compactdrawers.client.model;

import com.jaquadro.minecraft.chameleon.model.PassLimitedModel;
import com.jaquadro.minecraft.chameleon.model.ProxyBuilderModel;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGeometry;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.modeldata.DrawerStateModelData;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerDecoratorModel;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.samtrion.compactdrawers.block.BlockCompactDrawerBase;

public final class CompactDrawerBuilderModel<T extends Comparable<T>> extends ProxyBuilderModel implements IBakedModel {

    private ItemHandler     itemHandler;
    private PropertyEnum<?> property;

    public CompactDrawerBuilderModel(IBakedModel parent, ItemHandler itemHandler, PropertyEnum<?> property) {
        super(parent);
        this.itemHandler = itemHandler;
        this.property = property;
    }

    @Override
    protected IBakedModel buildModel(IBlockState state, IBakedModel parent) {
        try {
            if (!(state instanceof IExtendedBlockState)) {
                return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
            }
            IExtendedBlockState xstate = (IExtendedBlockState) state;
            IDrawerGeometry drawer = (IDrawerGeometry) xstate.getValue(this.property);
            if (drawer == null) {
                return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
            }
            EnumFacing dir = xstate.getValue(BlockCompactDrawerBase.FACING);

            DrawerStateModelData stateModel = xstate.getValue(BlockDrawers.STATE_MODEL);

            if (!DrawerDecoratorModel.shouldHandleState(stateModel)) {
                return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
            }

            return new DrawerDecoratorModel(parent, xstate, drawer, dir, stateModel);
        }
        catch (Throwable t) {
            return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
        }
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.itemHandler;
    }
}
