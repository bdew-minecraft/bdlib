package net.bdew.lib.network

import net.minecraft.nbt.CompoundNBT

import java.io.{ObjectInputStream, ObjectOutputStream}

class CompoundNBTSerialize(var tag: CompoundNBT = new CompoundNBT) extends Serializable {
  private def writeObject(out: ObjectOutputStream): Unit = {
    out.writeObject(NBTHelper.toBytes(tag))
  }

  private def readObject(in: ObjectInputStream): Unit = {
    tag = NBTHelper.fromBytes(in.readObject().asInstanceOf[Array[Byte]])
  }
}

object CompoundNBTSerialize {

  import scala.language.implicitConversions

  implicit def ser2content(v: CompoundNBTSerialize): CompoundNBT = v.tag
  implicit def content2ser(v: CompoundNBT): CompoundNBTSerialize = new CompoundNBTSerialize(v)
}
