/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.network

import java.io.{BufferedInputStream, ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.util.zip.GZIPInputStream

import net.minecraft.nbt.{CompressedStreamTools, NBTSizeTracker, NBTTagCompound}

object NBTHelper {
  def toBytes(t: NBTTagCompound): Array[Byte] = {
    val bs = new ByteArrayOutputStream()
    CompressedStreamTools.writeCompressed(t, bs)
    bs.toByteArray
  }

  def fromBytes(v: Array[Byte], maxSize: Int = 1024 * 1024) = {
    val is = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(v))))
    try {
      CompressedStreamTools.read(is, new NBTSizeTracker(maxSize))
    } finally {
      is.close()
    }
  }
}
