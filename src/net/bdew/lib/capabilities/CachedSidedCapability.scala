/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.minecraft.util.EnumFacing

class CachedSidedCapability[T](facory: EnumFacing => T) extends PartialFunction[EnumFacing, T] {
  lazy val map = (EnumFacing.VALUES map (f => f -> facory(f))).toMap + ((null, facory(null)))
  override def isDefinedAt(x: EnumFacing): Boolean = true
  override def apply(v: EnumFacing): T = map(v)
}
