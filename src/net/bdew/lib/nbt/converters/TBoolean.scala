/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType

object TBoolean extends ConvertedType[Boolean, Byte] {
  override def encode(v: Boolean) = if (v) 1.toByte else 0.toByte
  override def decode(v: Byte) = Some(v > 0.toByte)
}
