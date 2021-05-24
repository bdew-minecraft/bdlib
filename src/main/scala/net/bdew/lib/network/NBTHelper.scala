package net.bdew.lib.network

import net.minecraft.nbt.{CompoundNBT, CompressedStreamTools, NBTSizeTracker}

import java.io.{BufferedInputStream, ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.util.zip.GZIPInputStream

object NBTHelper {
  def toBytes(t: CompoundNBT): Array[Byte] = {
    val bs = new ByteArrayOutputStream()
    CompressedStreamTools.writeCompressed(t, bs)
    bs.toByteArray
  }

  def fromBytes(v: Array[Byte], maxSize: Int = 1024 * 1024): CompoundNBT = {
    val is = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(v))))
    try {
      CompressedStreamTools.read(is, new NBTSizeTracker(maxSize))
    } finally {
      is.close()
    }
  }
}
