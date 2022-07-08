package net.bdew.lib.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.{DefaultVertexFormat, PoseStack, Tesselator, VertexFormat}
import com.mojang.math.Vector3f
import net.bdew.lib.Client
import net.bdew.lib.render.models.ModelUtils
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
  def renderWorldBlockIntoGUI(m: PoseStack, world: BlockAndTintGetter, pos: BlockPos, face: Direction, rect: Rect): Unit = {
    // Warning, hackery incoming.
    // This is bastardized from both item and block drawing code in vanilla as neither does exactly what we need.

    val dispatcher = Client.minecraft.getBlockRenderer
    val textures = Client.textureManager
    val blockState = world.getBlockState(pos)
    val tessellator = Tesselator.getInstance()
    val buffer = tessellator.getBuilder

    // Setup shader stuff
    RenderSystem.setShader(() => GameRenderer.getBlockShader)

    // Make sure that block atlas is selected
    RenderSystem.setShaderTexture(0, Client.blocksAtlas)
    textures.getTexture(Client.blocksAtlas).setBlurMipmap(false, false)

    m.pushPose()
    RenderSystem.disableDepthTest()

    // Move to center of render area
    m.translate(rect.x1 + rect.w / 2, rect.y1 + rect.h / 2, 100.0F)

    // Scale to size of render area
    m.scale(rect.w, rect.h, 1)

    // GUI Y coordinates are top->down, while for blocks it's bottom->up, flip everything
    m.mulPose(Vector3f.XP.rotationDegrees(180))

    // Rotate to the face we are rendering
    face match {
      case Direction.NORTH =>
        m.mulPose(Vector3f.YP.rotationDegrees(180))
      case Direction.SOUTH =>
      // nothing
      case Direction.WEST =>
        m.mulPose(Vector3f.YP.rotationDegrees(90))
      case Direction.EAST =>
        m.mulPose(Vector3f.YP.rotationDegrees(270))
      case Direction.UP =>
        m.mulPose(Vector3f.XP.rotationDegrees(90))
      case Direction.DOWN =>
        m.mulPose(Vector3f.XP.rotationDegrees(270))
      case _ =>
    }

    // And go back to corner
    m.translate(-0.5f, -0.5f, -0.5f)

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
        buffer.putBulkData(m.last(), quad, color.r, color.g, color.b, 15728880, OverlayTexture.NO_OVERLAY)
      }
    }

    // And we're done!
    tessellator.end()
    textures.getTexture(Client.blocksAtlas).restoreLastBlurMipmap()
    m.popPose()
    RenderSystem.enableDepthTest()
  }

}
