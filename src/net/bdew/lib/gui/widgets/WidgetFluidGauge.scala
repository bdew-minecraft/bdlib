/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Texture, Point, Rect}
import net.bdew.lib.data.DataSlotTankBase
import scala.collection.mutable
import java.text.DecimalFormat
import net.bdew.lib.Misc
import org.lwjgl.opengl.GL11

class WidgetFluidGauge(val rect: Rect, overlay: Texture, dslot: DataSlotTankBase) extends Widget {
  val formater = new DecimalFormat("#,###")

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (dslot.getFluid != null) {
      tip += dslot.getFluid.getFluid.getLocalizedName(dslot.getFluid)
      tip += "%s/%s mB".format(formater.format(dslot.getFluidAmount), formater.format(dslot.getCapacity))
    } else {
      tip += Misc.toLocal("bdlib.label.empty")
    }
  }

  override def draw(mouse: Point) {
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    val fstack = dslot.getFluid
    if (fstack != null && fstack.getFluid.getStillIcon != null) {
      val icon = Texture(Texture.BLOCKS, fstack.getFluid.getStillIcon)
      var fillHeight = if (dslot.getCapacity > 0) rect.h * fstack.amount / dslot.getCapacity else 0
      var yStart: Int = 0

      while (fillHeight > 0) {
        if (fillHeight > 16) {
          parent.drawTexture(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon)
          fillHeight -= 16
        } else {
          parent.drawTextureInterpolate(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon, 0, 1 - fillHeight / 16, 1, 1)
          fillHeight = 0
        }
        yStart = yStart + 16
      }
    }

    GL11.glDisable(GL11.GL_BLEND)

    parent.drawTexture(rect, overlay)
  }
}
