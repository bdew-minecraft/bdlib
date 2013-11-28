/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.util.ResourceLocation

class TextureLocation(val resource: ResourceLocation, val p: Point) extends Point(p.x, p.y) {
  def this(resource: ResourceLocation, x: Int, y: Int) = this(resource, new Point(x, y))
}
