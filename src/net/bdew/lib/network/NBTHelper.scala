/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.network

import net.minecraft.nbt.{CompressedStreamTools, NBTSizeTracker, NBTTagCompound}

object NBTHelper {
  def toBytes(t: NBTTagCompound): Array[Byte] =
    CompressedStreamTools.compress(t)

  def fromBytes(v: Array[Byte], maxSize: Int = 1024 * 1024) =
    CompressedStreamTools.func_152457_a(v, new NBTSizeTracker(maxSize))
}
