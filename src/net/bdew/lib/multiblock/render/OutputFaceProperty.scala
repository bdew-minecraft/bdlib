/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.render

import net.bdew.lib.property.SimpleUnlistedProperty
import net.minecraft.util.EnumFacing

/**
  * Used for rendering output overlays, the integer will be used as tint index for coloring
  */
object OutputFaceProperty extends SimpleUnlistedProperty("output_faces", classOf[Map[EnumFacing, Int]])
