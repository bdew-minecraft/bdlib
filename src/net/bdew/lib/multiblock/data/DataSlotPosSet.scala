/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos

import scala.collection.mutable

case class DataSlotPosSet(name: String, parent: DataSlotContainer) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockPos]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    t.setList(name, set.toList)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    set.clear()
    set ++= t.getList[BlockPos](name)
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotPosSet {

  import scala.language.implicitConversions

  implicit def dataSlotPosSet2set(v: DataSlotPosSet): mutable.Set[BlockPos] = v.set
}
