/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.block.BlockFace
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection

import scala.collection.mutable

case class DataSlotBlockFaceMap(name: String, parent: DataSlotContainer) extends DataSlot {
  val map = collection.mutable.Map.empty[BlockFace, Int]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def inverted = map.map(_.swap)

  def ent2arr(x: (BlockFace, Int)) = Array(x._2, x._1.x, x._1.y, x._1.z, x._1.face.ordinal())
  def arr2ent(x: Array[Int]) = BlockFace(x(1), x(2), x(3), ForgeDirection.values()(x(4))) -> x(0)

  import net.bdew.lib.nbt._

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    t.setList(name, map.map(ent2arr))
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    map.clear()
    map ++= t.getList[Array[Int]](name) map arr2ent
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotBlockFaceMap {

  import scala.language.implicitConversions

  implicit def dataSlotBlockFaceMap2map(v: DataSlotBlockFaceMap): mutable.Map[BlockFace, Int] = v.map
}
