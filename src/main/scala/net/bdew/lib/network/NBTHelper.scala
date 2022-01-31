package net.bdew.lib.network

import net.minecraft.nbt.{CompoundTag, NbtAccounter, NbtIo}

import java.io.{BufferedInputStream, ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.util.zip.GZIPInputStream

object NBTHelper {
  def toBytes(t: CompoundTag): Array[Byte] = {
    val bs = new ByteArrayOutputStream()
    NbtIo.writeCompressed(t, bs)
    bs.toByteArray
  }

  def fromBytes(v: Array[Byte], maxSize: Int = 1024 * 1024): CompoundTag = {
    val is = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(v))))
    try {
      NbtIo.read(is, new NbtAccounter(maxSize))
    } finally {
      is.close()
    }
  }
}
