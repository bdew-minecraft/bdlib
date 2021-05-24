package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Client
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.minecraft.client.audio.SimpleSound
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetButtonIcon(p: Point, clicked: WidgetButtonIcon => Unit, baseTex: Texture, hoverTex: Texture) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p + (1, 1), 14, 14)

  def icon: Texture = null
  def hover: ITextComponent = null

  override def drawBackground(m: MatrixStack, mouse: Point): Unit = {
    if (rect.contains(mouse))
      parent.drawTexture(m, rect, hoverTex)
    else
      parent.drawTexture(m, rect, baseTex)
  }

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    if (icon != null)
      parent.drawTexture(m, iconRect, icon)
  }

  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit = {
    if (hover != null)
      tip += hover
  }

  override def mouseClicked(p: Point, button: Int): Boolean = {
    Client.soundManager.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    clicked(this)
    true
  }
}
