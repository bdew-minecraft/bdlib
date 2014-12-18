/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.network

import java.io.{ObjectInputStream, ObjectOutputStream}

import net.minecraft.nbt.NBTTagCompound

class NBTTagCompoundSerialize(var tag: NBTTagCompound = new NBTTagCompound) extends Serializable {

  private def writeObject(out: ObjectOutputStream) {
    out.writeObject(NBTHelper.toBytes(tag))
  }

  private def readObject(in: ObjectInputStream) {
    tag = NBTHelper.fromBytes(in.readObject().asInstanceOf[Array[Byte]])
  }
}

object NBTTagCompoundSerialize {

  import scala.language.implicitConversions

  implicit def ser2content(v: NBTTagCompoundSerialize): NBTTagCompound = v.tag
  implicit def content2ser(v: NBTTagCompound): NBTTagCompoundSerialize = new NBTTagCompoundSerialize(v)
}
