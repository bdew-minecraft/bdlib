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

import net.minecraft.item.{Item, ItemStack}

class ItemStackSerialize(var stack: ItemStack) extends Serializable {
  private def writeObject(out: ObjectOutputStream) {
    out.writeShort(Item.getIdFromItem(stack.getItem))
    out.writeByte(stack.stackSize)
    out.writeShort(stack.getItemDamage)
    if (stack.hasTagCompound)
      out.writeObject(NBTHelper.toBytes(stack.getTagCompound))
    else
      out.writeObject(null)
  }

  private def readObject(in: ObjectInputStream) {
    val id = in.readShort()
    val sz = in.readByte()
    val dmg = in.readShort()
    stack = new ItemStack(Item.getItemById(id), sz, dmg)
    val obj = in.readObject().asInstanceOf[Array[Byte]]
    if (obj != null)
      stack.setTagCompound(NBTHelper.fromBytes(obj))
  }
}

object ItemStackSerialize {

  import scala.language.implicitConversions

  implicit def ser2content(v: ItemStackSerialize): ItemStack = v.stack
  implicit def content2ser(v: ItemStack): ItemStackSerialize = new ItemStackSerialize(v)
}

