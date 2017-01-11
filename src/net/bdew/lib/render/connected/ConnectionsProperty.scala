/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.property.SimpleUnlistedProperty
import net.minecraft.util.math.Vec3i

/**
  * Used for rendering borders.
  * Vectors should be (-1,-1,-1) to (+1,+1,+1) excluding the block itself (0,0,0)
  * Value is true if the block is part of the same structure
  */
object ConnectionsProperty extends SimpleUnlistedProperty("connections", classOf[Map[Vec3i, Boolean]])