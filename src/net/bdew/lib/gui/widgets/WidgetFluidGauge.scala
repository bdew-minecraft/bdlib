/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, TextureLocation, Rect}
import net.bdew.lib.data.DataSlotTankBase
import scala.collection.mutable
import net.minecraft.client.renderer.texture.TextureMap
import java.text.DecimalFormat
import net.bdew.lib.Misc
import org.lwjgl.opengl.GL11

class WidgetFluidGauge(val rect: Rect, overlay: TextureLocation, dslot: DataSlotTankBase) extends Widget {
  val formater = new DecimalFormat("#,###")

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (dslot.getFluid != null) {
      tip += dslot.getFluid.getFluid.getLocalizedName
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
      val icon = fstack.getFluid.getStillIcon
      bindTexture(TextureMap.locationBlocksTexture)
      var fillHeight: Int = if (dslot.getCapacity > 0) (rect.h * fstack.amount / dslot.getCapacity).round else 0
      var yStart: Int = 0

      while (fillHeight > 0) {
        if (fillHeight > 16) {
          parent.drawIcon(Rect(rect.x, rect.y + rect.h - 16 - yStart, rect.w, 16), icon)
          fillHeight -= 16
        } else {
          parent.drawIcon(Rect(rect.x, rect.y + rect.h - fillHeight - yStart, rect.w, fillHeight), icon)
          fillHeight = 0
        }
        yStart = yStart + 16
      }
    }

    GL11.glDisable(GL11.GL_BLEND)

    parent.drawTexture(rect, overlay)
  }
}
