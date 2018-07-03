package net.samtrion.compactdrawers.client.model;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerSealedModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public final class ItemHandler extends ItemOverrideList {
    public ItemHandler() {
        super(ImmutableList.<ItemOverride>of());
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBakedModel handleItemState(IBakedModel parent, @Nonnull ItemStack stack, World world, EntityLivingBase entity) {
        if (stack.isEmpty())
            return parent;

        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("tile", Constants.NBT.TAG_COMPOUND))
            return parent;

        Block block = Block.getBlockFromItem(stack.getItem());
        IBlockState state = block.getStateFromMeta(stack.getMetadata());

        return new DrawerSealedModel(parent, state, true);
    }
}