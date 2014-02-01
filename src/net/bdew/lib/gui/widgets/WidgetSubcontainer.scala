/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui._
import net.minecraft.util.Icon
import net.bdew.lib.gui.Rect

class WidgetSubcontainer(val rect: Rect) extends BaseWidget with WidgetContainer {
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = parent.getOffsetFromWindow + rect.origin
  def drawTexture(r: Rect, uv: Point, color: Color = Color.white) = parent.drawTexture(r, uv, color)
  def drawIcon(r: Rect, i: Icon, color: Color = Color.white) = parent.drawIcon(r, i, color)
  def drawTextureScaled(r: Rect, t: TextureLocationScaled, color: Color = Color.white) = parent.drawTextureScaled(r, t,color)
}
