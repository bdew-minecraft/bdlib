/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetPowerGauge(val rect: Rect, texture: Texture, dSlot: DataSlotPower) extends Widget {
  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    val fill = dSlot.stored / dSlot.capacity
    parent.drawTextureInterpolate(m, rect, texture, 0, 1 - fill, 1, 1)
  }

  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit =
    tip += Text.energyCap(dSlot.stored, dSlot.capacity)
}
