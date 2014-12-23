/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.data.DataSlotTankBase
import net.bdew.lib.gui.{Color, Point, Rect, Texture}
import net.bdew.lib.{DecFormat, Misc}
import org.lwjgl.opengl.GL11

import scala.collection.mutable

class WidgetFluidGauge(val rect: Rect, overlay: Texture, dSlot: DataSlotTankBase) extends Widget {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (dSlot.getFluid != null) {
      tip += dSlot.getFluid.getFluid.getLocalizedName(dSlot.getFluid)
      tip += "%s/%s mB".format(DecFormat.round(dSlot.getFluidAmount), DecFormat.round(dSlot.getCapacity))
    } else {
      tip += Misc.toLocal("bdlib.label.empty")
    }
  }

  override def draw(mouse: Point) {
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    val fStack = dSlot.getFluid
    if (fStack != null) {
      val color = Color.fromInt(Misc.getFluidColor(fStack))
      val icon = Texture(Texture.BLOCKS, Misc.getFluidIcon(fStack))
      var fillHeight = if (dSlot.getCapacity > 0) rect.h * fStack.amount / dSlot.getCapacity else 0
      var yStart: Int = 0

      while (fillHeight > 0) {
        if (fillHeight > 16) {
          parent.drawTexture(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon, color)
          fillHeight -= 16
        } else {
          parent.drawTextureInterpolate(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon, 0, 1 - fillHeight / 16, 1, 1, color)
          fillHeight = 0
        }
        yStart = yStart + 16
      }
    }

    GL11.glColor3d(1, 1, 1)
    GL11.glDisable(GL11.GL_BLEND)

    if (overlay != null)
      parent.drawTexture(rect, overlay)
  }
}
