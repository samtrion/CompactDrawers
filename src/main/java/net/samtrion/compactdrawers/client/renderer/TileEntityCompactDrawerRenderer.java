package net.samtrion.compactdrawers.client.renderer;

import java.util.List;

import javax.annotation.Nonnull;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.geometry.Area2D;
import com.jaquadro.minecraft.chameleon.render.*;
import com.jaquadro.minecraft.chameleon.resources.IconRegistry;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.render.IRenderLabel;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.dynamic.StatusModelData;
import com.jaquadro.minecraft.storagedrawers.block.dynamic.StatusModelData.Slot;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerSealedModel;
import com.jaquadro.minecraft.storagedrawers.item.EnumUpgradeStatus;
import com.jaquadro.minecraft.storagedrawers.util.CountFormatter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class TileEntityCompactDrawerRenderer extends TileEntitySpecialRenderer<TileEntityDrawers> {
    private boolean[]   renderAsBlock = new boolean[4];
    private ItemStack[] renderStacks  = new ItemStack[4];

    private RenderItem  renderItem;

    @Override
    public void render(TileEntityDrawers tile, double x, double y, double z, float partialTickTime, int destroyStage, float par7) {
        if (tile == null)
            return;

        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state == null) {
            return;
        }

        if (!(state.getBlock() instanceof BlockDrawers)) {
            return;
        }

        BlockDrawers block = (BlockDrawers) state.getBlock();
        float depth = block.isHalfDepth(state) ? .5f : 1f;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        renderItem = Minecraft.getMinecraft().getRenderItem();

        EnumFacing side = EnumFacing.getFront(tile.getDirection());
        int ambLight = getWorld().getCombinedLight(tile.getPos().offset(side), 0);
        int lu = ambLight % 65536;
        int lv = ambLight / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lu / 1.0F, (float) lv / 1.0F);

        ChamRender renderer = ChamRenderManager.instance.getRenderer(Tessellator.getInstance().getBuffer());

        Minecraft mc = Minecraft.getMinecraft();
        boolean cache = mc.gameSettings.fancyGraphics;
        mc.gameSettings.fancyGraphics = true;
        renderUpgrades(renderer, tile, state);
        if (!tile.getDrawerAttributes().isConcealed() && !tile.isSealed())
            renderFastItemSet(renderer, tile, state, side, depth, partialTickTime);

        mc.gameSettings.fancyGraphics = cache;

        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableNormalize();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

        ChamRenderManager.instance.releaseRenderer(renderer);
    }

    @SuppressWarnings("deprecation")
    private void renderFastItemSet(ChamRender renderer, TileEntityDrawers tile, IBlockState state, EnumFacing side, float depth, float partialTickTime) {
        int drawerCount = tile.getDrawerCount();

        for (int i = 0; i < drawerCount; i++) {
            renderStacks[i] = ItemStack.EMPTY;
            IDrawer drawer = tile.getDrawer(i);
            if (!drawer.isEnabled() || drawer.isEmpty())
                continue;

            ItemStack itemStack = drawer.getStoredItemPrototype();
            renderStacks[i] = itemStack;
            renderAsBlock[i] = isItemBlockType(itemStack);
        }

        for (int i = 0; i < drawerCount; i++) {
            if (!renderStacks[i].isEmpty() && !renderAsBlock[i])
                renderFastItem(renderer, renderStacks[i], tile, state, i, side, depth, partialTickTime);
        }

        for (int i = 0; i < drawerCount; i++) {
            if (!renderStacks[i].isEmpty() && renderAsBlock[i])
                renderFastItem(renderer, renderStacks[i], tile, state, i, side, depth, partialTickTime);
        }

        if (tile.getDrawerAttributes().isShowingQuantity()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            BlockPos blockPos = tile.getPos().add(.5, .5, .5);
            double distance = Math.sqrt(blockPos.distanceSq(player.getPosition()));

            float alpha = 1;
            if (distance > 4)
                alpha = Math.max(1f - (float) ((distance - 4) / 6), 0.05f);

            if (distance < 10) {
                for (int i = 0; i < drawerCount; i++)
                    renderText(CountFormatter.format(getFontRenderer(), tile.getDrawer(i)), tile, state, i, side, depth, alpha);
            }
        }
    }

    private void renderText(String text, TileEntityDrawers tile, IBlockState state, int slot, EnumFacing side, float depth, float alpha) {
        if (text == null || text.isEmpty())
            return;

        BlockDrawers block = (BlockDrawers) state.getBlock();
        StatusModelData statusInfo = block.getStatusInfo(state);
        float frontDepth = (float) statusInfo.getFrontDepth() * .0625f;
        int textWidth = getFontRenderer().getStringWidth(text);

        Area2D statusArea = statusInfo.getSlot(slot).getLabelArea();
        float x = (float) (statusArea.getX() + statusArea.getWidth() / 2);
        float y = 16f - (float) statusArea.getY() - (float) statusArea.getHeight();

        GlStateManager.pushMatrix();
        alignRendering(side);
        moveRendering(.125f, x, y, 1f - depth + frontDepth - .005f);

        GlStateManager.disableLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.doPolygonOffset(-1, -20);

        getFontRenderer().drawString(text, -textWidth / 2, 0, (int) (255 * alpha) << 24 | 255 << 16 | 255 << 8 | 255);

        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    @SuppressWarnings("deprecation")
    private void renderFastItem(ChamRender renderer, @Nonnull ItemStack itemStack, TileEntityDrawers tile, IBlockState state, int slot, EnumFacing side, float depth, float partialTickTime) {
        int drawerCount = tile.getDrawerCount();
        float size = (drawerCount == 1) ? .5f : .25f;

        BlockDrawers block = (BlockDrawers) state.getBlock();
        StatusModelData statusInfo = block.getStatusInfo(state);
        float frontDepth = (float) statusInfo.getFrontDepth() * .0625f;
        Area2D slotArea = statusInfo.getSlot(slot).getSlotArea();

        GlStateManager.pushMatrix();

        float xCenter = (float) slotArea.getX() + (float) slotArea.getWidth() / 2 - (8 * size);
        float yCenter = 16 - (float) slotArea.getY() - (float) slotArea.getHeight() / 2 - (8 * size);

        alignRendering(side);
        moveRendering(size, xCenter, yCenter, 1f - depth + frontDepth - .005f);

        List<IRenderLabel> renderHandlers = StorageDrawers.renderRegistry.getRenderHandlers();
        for (IRenderLabel renderHandler : renderHandlers) {
            renderHandler.render(tile, tile.getGroup(), slot, 0, partialTickTime);
        }
        GlStateManager.pushMatrix();
        if (drawerCount == 1) {
            GlStateManager.scale(2.6f, 2.6f, 1);
            GlStateManager.rotate(171.6f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(84.9f, 1.0F, 0.0F, 0.0F);
        }
        else {
            GlStateManager.scale(1.92f, 1.92f, 1);
            GlStateManager.rotate(169.2f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(79.0f, 1.0F, 0.0F, 0.0F);
        }
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -1);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushAttrib();
        GlStateManager.enableRescaleNormal();
        GlStateManager.popAttrib();

        try {
            renderItem.renderItemIntoGUI(itemStack, 0, 0);
        }
        catch (Exception e) {
            // Shrug
        }

        GlStateManager.disableBlend(); // Clean up after RenderItem
        GlStateManager.enableAlpha(); // Restore world render state after RenderItem

        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
    }

    private boolean isItemBlockType(@Nonnull ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock && renderItem.shouldRenderItemIn3D(itemStack);
    }

    private void alignRendering(EnumFacing side) {
        GlStateManager.translate(.5f, .5f, .5f);
        GlStateManager.rotate(getRotationYForSide2D(side), 0, 1, 0);
        GlStateManager.translate(-.5f, -.5f, -.5f);
    }

    private void moveRendering(float size, float offsetX, float offsetY, float offsetZ) {
        GlStateManager.translate(0, 1, 1 - offsetZ);
        GlStateManager.scale(1 / 16f, -1 / 16f, 0.00001);

        GlStateManager.translate(offsetX, offsetY, 0.);
        GlStateManager.scale(size, size, 1);
    }

    private static final float[] sideRotationY2D = { 0, 0, 2, 0, 3, 1 };

    private float getRotationYForSide2D(EnumFacing side) {
        return sideRotationY2D[side.ordinal()] * 90;
    }

    private void renderUpgrades(ChamRender renderer, TileEntityDrawers tile, IBlockState state) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.enableAlpha();

        renderIndicator(renderer, tile, state, tile.getDirection(), tile.upgrades().getStatusType());
        renderTape(renderer, tile, state, tile.getDirection(), tile.isSealed());
    }

    @SuppressWarnings("deprecation")
    private void renderIndicator(ChamRender renderer, TileEntityDrawers tile, IBlockState blockState, int side, EnumUpgradeStatus level) {
        if (level == null || side < 2 || side > 5)
            return;

        BlockDrawers block = (BlockDrawers) blockState.getBlock();
        StatusModelData statusInfo = block.getStatusInfo(blockState);
        if (statusInfo == null)
            return;

        double depth = block.isHalfDepth(blockState) ? .5 : 1;
        int count = (tile instanceof TileEntityDrawersComp) ? 1 : block.getDrawerCount(blockState);

        double unit = 0.0625;
        double frontDepth = statusInfo.getFrontDepth() * unit;
        IconRegistry ico = Chameleon.instance.iconRegistry;

        for (int i = 0; i < count; i++) {
            IDrawer drawer = tile.getDrawer(i);
            if (drawer == null || tile.getDrawerAttributes().isConcealed())
                continue;

            Slot slot = statusInfo.getSlot(i);
            TextureAtlasSprite iconOff = ico.getIcon(slot.getOffResource(level));
            TextureAtlasSprite iconOn = ico.getIcon(slot.getOnResource(level));

            Area2D statusArea = slot.getStatusArea();

            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(-1, -1);

            renderer.setRenderBounds(statusArea.getX() * unit, statusArea.getY() * unit, 0, (statusArea.getX() + statusArea.getWidth()) * unit, (statusArea.getY() + statusArea.getHeight()) * unit, depth - frontDepth);
            renderer.state.setRotateTransform(ChamRender.ZPOS, side);
            renderer.renderFace(ChamRender.FACE_ZPOS, null, blockState, BlockPos.ORIGIN, iconOff, 1, 1, 1);
            renderer.state.clearRotateTransform();

            GlStateManager.doPolygonOffset(-1, -10);

            if (level == EnumUpgradeStatus.LEVEL1 && !drawer.isEmpty() && drawer.getRemainingCapacity() == 0) {
                renderer.setRenderBounds(statusArea.getX() * unit, statusArea.getY() * unit, 0, (statusArea.getX() + statusArea.getWidth()) * unit, (statusArea.getY() + statusArea.getHeight()) * unit, depth - frontDepth);
                renderer.state.setRotateTransform(ChamRender.ZPOS, side);
                renderer.renderFace(ChamRender.FACE_ZPOS, null, blockState, BlockPos.ORIGIN, iconOn, 1, 1, 1);
                renderer.state.clearRotateTransform();
            }
            else if (level == EnumUpgradeStatus.LEVEL2) {
                Area2D activeArea = slot.getStatusActiveArea();
                int stepX = slot.getActiveStepsX();
                int stepY = slot.getActiveStepsY();

                double indXStart = activeArea.getX();
                double indXEnd = activeArea.getX() + activeArea.getWidth();
                double indXCur = (stepX == 0) ? indXEnd : getIndEnd(block, tile, i, indXStart, activeArea.getWidth(), stepX);

                double indYStart = activeArea.getY();
                double indYEnd = activeArea.getY() + activeArea.getHeight();
                double indYCur = (stepY == 0) ? indYEnd : getIndEnd(block, tile, i, indYStart, activeArea.getHeight(), stepY);

                if (indXCur > indXStart && indYCur > indYStart) {
                    indXCur = Math.min(indXCur, indXEnd);
                    indYCur = Math.min(indYCur, indYEnd);

                    renderer.setRenderBounds(indXStart * unit, indYStart * unit, 0, indXCur * unit, indYCur * unit, depth - frontDepth);
                    renderer.state.setRotateTransform(ChamRender.ZPOS, side);
                    renderer.renderFace(ChamRender.FACE_ZPOS, null, blockState, BlockPos.ORIGIN, iconOn, 1, 1, 1);
                    renderer.state.clearRotateTransform();
                }
            }

            GlStateManager.disablePolygonOffset();
        }
    }

    private void renderTape(ChamRender renderer, TileEntityDrawers tile, IBlockState blockState, int side, boolean taped) {
        if (!taped || side < 2 || side > 5)
            return;

        BlockDrawers block = (BlockDrawers) blockState.getBlock();
        double depth = block.isHalfDepth(blockState) ? .5 : 1;
        TextureAtlasSprite iconTape = Chameleon.instance.iconRegistry.getIcon(DrawerSealedModel.iconTapeCover);

        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -1);

        renderer.setRenderBounds(0, 0, 0, 1, 1, depth);
        renderer.state.setRotateTransform(ChamRender.ZPOS, side);
        renderer.renderPartialFace(ChamRender.FACE_ZPOS, null, blockState, BlockPos.ORIGIN, iconTape, 0, 0, 1, 1, 1, 1, 1);
        renderer.state.clearRotateTransform();

        GlStateManager.disablePolygonOffset();
    }

    @SuppressWarnings("deprecation")
    private double getIndEnd(BlockDrawers block, TileEntityDrawers tile, int slot, double x, double w, int step) {
        IDrawer drawer = tile.getDrawer(slot);
        if (drawer == null)
            return x;

        int cap = drawer.getMaxCapacity();
        int count = drawer.getStoredItemCount();
        if (cap == 0 || count == 0)
            return x;

        float fillAmt = (float) (step * count / cap) / step;

        return x + (w * fillAmt);
    }
}