package net.bdew.lib.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.{DefaultVertexFormat, Tesselator, VertexFormat}
import com.mojang.math.Axis
import net.bdew.lib.Client
import net.bdew.lib.render.models.ModelUtils
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraftforge.client.model.data.ModelData

import scala.jdk.CollectionConverters._

object ModelDrawHelper {

  /**
   * Draws a face from a block, as seen in the world into GUI
   *
   * @param world world object
   * @param pos   position of block
   * @param face  face of block
   * @param rect  area in GUI to draw into
   */
  def renderWorldBlockIntoGUI(graphics: GuiGraphics, world: BlockAndTintGetter, pos: BlockPos, face: Direction, rect: Rect): Unit = {
    // Warning, hackery incoming.
    // This is bastardized from both item and block drawing code in vanilla as neither does exactly what we need.

    val dispatcher = Client.minecraft.getBlockRenderer
    val textures = Client.textureManager
    val blockState = world.getBlockState(pos)
    val tessellator = Tesselator.getInstance()
    val buffer = tessellator.getBuilder
    val pose = graphics.pose()

    // Setup shader stuff
    RenderSystem.setShader(() => GameRenderer.getPositionColorTexShader)

    // Make sure that block atlas is selected
    RenderSystem.setShaderTexture(0, Client.blocksAtlas)
    textures.getTexture(Client.blocksAtlas).setBlurMipmap(false, false)

    pose.pushPose()
    RenderSystem.disableDepthTest()

    // Move to center of render area
    pose.translate(rect.x1 + rect.w / 2, rect.y1 + rect.h / 2, 100.0F)

    // Scale to size of render area
    pose.scale(rect.w, rect.h, 1)

    // GUI Y coordinates are top->down, while for blocks it's bottom->up, flip everything
    pose.mulPose(Axis.XP.rotationDegrees(180))

    // Rotate to the face we are rendering
    face match {
      case Direction.NORTH =>
        pose.mulPose(Axis.YP.rotationDegrees(180))
      case Direction.SOUTH =>
      // nothing
      case Direction.WEST =>
        pose.mulPose(Axis.YP.rotationDegrees(90))
      case Direction.EAST =>
        pose.mulPose(Axis.YP.rotationDegrees(270))
      case Direction.UP =>
        pose.mulPose(Axis.XP.rotationDegrees(90))
      case Direction.DOWN =>
        pose.mulPose(Axis.XP.rotationDegrees(270))
      case _ =>
    }

    // And go back to corner
    pose.translate(-0.5f, -0.5f, -0.5f)

    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK)

    val model = dispatcher.getBlockModel(blockState)

    val te = world.getBlockEntity(pos)
    val data = if (te != null) te.getModelData else ModelData.EMPTY
    val rand = RandomSource.create()

    val renderTypes = model.getRenderTypes(blockState, rand, data).asScala

    // If blocks renders in multiple layers - go through all
    for (renderType <- renderTypes) {

      // Grab all relevant quads
      for (quad <- ModelUtils.getAllQuads(model, blockState, rand, data, renderType)) {
        val color = if (quad.isTinted)
          Color.fromInt(Client.blockColors.getColor(blockState, world, pos, quad.getTintIndex))
        else
          Color.white
        buffer.putBulkData(pose.last(), quad, color.r, color.g, color.b, 15728880, OverlayTexture.NO_OVERLAY)
      }
    }

    // And we're done!
    tessellator.end()
    textures.getTexture(Client.blocksAtlas).restoreLastBlurMipmap()
    pose.popPose()
    RenderSystem.enableDepthTest()
  }

}
