/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

import scala.collection.mutable

case class DataSlotPosSet(name: String, parent: DataSlotContainer) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockRef]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val lst = new NBTTagList()
    for (x <- set) lst.appendTag(Misc.applyMutator(x.writeToNBT, new NBTTagCompound))
    t.setTag(name, lst)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    set.clear()
    set ++= Misc.iterNbtCompoundList(t, name) map BlockRef.fromNBT
  }

  def updated() = parent.dataSlotChanged(this)

}

object DataSlotPosSet {

  import scala.language.implicitConversions

  implicit def dataSlotPosSet2set(v: DataSlotPosSet): mutable.Set[BlockRef] = v.set
}
