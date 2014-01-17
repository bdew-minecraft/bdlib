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

  override def draw() {
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
          parent.drawTexturedModelRectFromIcon(rect.x, rect.y + rect.h - 16 - yStart, icon, 16, 16)
          fillHeight -= 16
        } else {
          parent.drawTexturedModelRectFromIcon(rect.x, rect.y + rect.h - fillHeight - yStart, icon, 16, fillHeight)
          fillHeight = 0
        }
        yStart = yStart + 16
      }
    }

    GL11.glDisable(GL11.GL_BLEND)

    bindTexture(overlay.resource)
    parent.drawTexturedModalRect(rect.x, rect.y, overlay.x, overlay.y, rect.w, rect.h)
  }
}
