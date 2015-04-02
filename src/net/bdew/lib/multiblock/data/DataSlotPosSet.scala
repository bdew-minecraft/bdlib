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
import net.minecraft.nbt.NBTTagCompound

import scala.collection.mutable

case class DataSlotPosSet(name: String, parent: DataSlotContainer) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockRef]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  import net.bdew.lib.nbt._

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    t.setList(name, set.map(x => Misc.applyMutator(x.writeToNBT, new NBTTagCompound)))
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    set.clear()
    set ++= t.getList[NBTTagCompound](name) map BlockRef.fromNBT
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotPosSet {

  import scala.language.implicitConversions

  implicit def dataSlotPosSet2set(v: DataSlotPosSet): mutable.Set[BlockRef] = v.set
}
