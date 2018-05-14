package net.samtrion.compactdrawers.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import com.jaquadro.minecraft.chameleon.resources.IItemVariantProvider;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemCompactDrawerBase extends ItemBlock implements IItemMeshMapper, IItemVariantProvider {
	protected ItemCompactDrawerBase(Block block) {
		super(block);
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
    public void addInformation (@Nonnull ItemStack itemStack, @Nullable World world, List<String> list, ITooltipFlag advanced) {
        int count = getBlockBaseStorage();
        list.add(I18n.format("storagedrawers.drawers.description", count));

        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("tile")) {
            list.add(ChatFormatting.YELLOW + I18n.format("storagedrawers.drawers.sealed"));
        }
    }
    
    protected abstract int getBlockBaseStorage();
}
