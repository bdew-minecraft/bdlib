/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.Constants

import scala.collection.mutable

case class DataSlotOutputConfig(name: String, parent: DataSlotContainer, slots: Int) extends DataSlot {
  val map = collection.mutable.Map.empty[Int, OutputConfig]

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def inverted: mutable.Map[OutputConfig, Int] = map.map(_.swap)

  def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    for ((n, x) <- map) {
      t.put(name + "_" + n, NBT.from { tmp =>
        x.write(tmp)
        tmp.putString("kind", x.id)
      })
    }
  }

  def load(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    map.clear()
    for (i <- 0 until slots) {
      if (t.contains(name + "_" + i, Constants.NBT.TAG_COMPOUND)) {
        val cfg = t.getCompound(name + "_" + i)
        val cfgObj = OutputConfigManager.create(cfg.getString("kind"))
        cfgObj.read(cfg)
        map += i -> cfgObj
      }
    }
  }

  def updated(): Unit = parent.dataSlotChanged(this)
}

object DataSlotOutputConfig {

  import scala.language.implicitConversions

  implicit def dataSlotOutputConfig2map(v: DataSlotOutputConfig): mutable.Map[Int, OutputConfig] = v.map
}
