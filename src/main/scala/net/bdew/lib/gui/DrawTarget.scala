package net.bdew.lib.gui

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex._
import net.minecraft.client.gui.{Font, GuiGraphics}
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

trait DrawTarget {
  def getZLevel: Float
  def getFontRenderer: Font
  def drawTexture(graphics: GuiGraphics, r: Rect, t: Texture, color: Color = Color.white): Unit
  def drawText(graphics: GuiGraphics, text: Component, p: Point, color: Color, shadow: Boolean): Unit
  def drawItem(graphics: GuiGraphics, item: ItemStack, p: Point): Unit

  def drawTextureInterpolate(graphics: GuiGraphics, r: Rect, t: Texture, x1: Float, y1: Float, x2: Float, y2: Float, color: Color = Color.white): Unit =
    drawTexture(graphics, r.interpolate(x1, y1, x2, y2), Texture.interpolate(t, x1, y1, x2, y2), color)

  def drawTextureTiled(graphics: GuiGraphics, r: Rect, t: Texture, tileX: Float, tileY: Float, color: Color = Color.white): Unit = {
    var leftX = r.w
    while (leftX > 0) {
      val xpos = r.x + r.w - leftX
      var leftY = r.h
      while (leftY > 0) {
        val xw = if (leftX > tileX) tileX else leftX
        val yw = if (leftY > tileY) tileY else leftY
        val ypos = r.y - yw + leftY
        val tt = Texture.interpolate(t, 0, 1 - yw / tileY, xw / tileX, 1)
        drawTexture(graphics, Rect(xpos, ypos, xw, yw), tt, color)
        leftY -= tileY
      }
      leftX -= tileX
    }
  }
}

trait SimpleDrawTarget extends DrawTarget {
  private final val tesselator = Tesselator.getInstance()

  override def drawTexture(graphics: GuiGraphics, r: Rect, t: Texture, color: Color = Color.white): Unit = {
    val z = getZLevel
    t.bind()
    color.activate()

    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader)

    val builder = tesselator.getBuilder
    val matrix = graphics.pose().last().pose()

    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
    builder.vertex(matrix, r.x1, r.y2, z).uv(t.u1, t.v2).endVertex()
    builder.vertex(matrix, r.x2, r.y2, z).uv(t.u2, t.v2).endVertex()
    builder.vertex(matrix, r.x2, r.y1, z).uv(t.u2, t.v1).endVertex()
    builder.vertex(matrix, r.x1, r.y1, z).uv(t.u1, t.v1).endVertex()

    BufferUploader.drawWithShader(builder.end())
  }

  override def drawText(graphics: GuiGraphics, text: Component, p: Point, color: Color, shadow: Boolean): Unit = {
    graphics.drawString(getFontRenderer, text, p.x.toInt, p.y.toInt, color.asARGB, shadow)
  }

  override def drawItem(graphics: GuiGraphics, item: ItemStack, p: Point): Unit = {
    val stack = RenderSystem.getModelViewStack
    stack.pushPose()
    stack.mulPoseMatrix(graphics.pose().last().pose())
    graphics.renderItem(item, p.x.toInt, p.y.toInt)
    stack.popPose()
  }
}
