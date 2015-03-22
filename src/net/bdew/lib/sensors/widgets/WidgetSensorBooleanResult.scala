/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.widgets

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.init.Blocks

import scala.collection.mutable

class WidgetSensorBooleanResult(val p: Point, state: => Boolean, stringPrefix: String) extends Widget {
  val rsOn = new IconWrapper(Texture.BLOCKS, Blocks.redstone_torch.getIcon(0, 0))
  val rsOff = new IconWrapper(Texture.BLOCKS, Blocks.unlit_redstone_torch.getIcon(0, 0))

  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) =
    if (state)
      tip += Misc.toLocal(stringPrefix + ".on")
    else
      tip += Misc.toLocal(stringPrefix + ".off")

  override def draw(mouse: Point) =
    if (state)
      parent.drawTexture(rect, rsOn)
    else
      parent.drawTexture(rect, rsOff)
}
