/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

import scala.collection.mutable

case class DataSlotOutputConfig(name: String, parent: DataSlotContainer, slots: Int) extends DataSlot {
  val map = collection.mutable.Map.empty[Int, OutputConfig]

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def inverted = map.map(_.swap)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    for ((n, x) <- map) {
      val tmp = new NBTTagCompound()
      x.write(tmp)
      tmp.setString("kind", x.id)
      t.setTag(name + "_" + n, tmp)
    }
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    map.clear()
    for (i <- 0 until slots) {
      if (t.hasKey(name + "_" + i)) {
        val cfg = t.getCompoundTag(name + "_" + i)
        val cfgObj = OutputConfigManager.create(cfg.getString("kind"))
        cfgObj.read(cfg)
        map += i -> cfgObj
      }
    }
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotOutputConfig {

  import scala.language.implicitConversions

  implicit def dataSlotOutputConfig2map(v: DataSlotOutputConfig): mutable.Map[Int, OutputConfig] = v.map
}
