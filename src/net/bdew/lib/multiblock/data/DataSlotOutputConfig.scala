/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.data.base.{DataSlot, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotOutputConfig(name: String, parent: TileDataSlots, slots: Int) extends DataSlot {
  val map = collection.mutable.Map.empty[Int, OutputConfig]

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def inverted = map.map(_.swap)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    for ((n, x) <- map) {
      val tmp = new NBTTagCompound()
      x.write(tmp)
      val kind = x match {
        case _: OutputConfigPower => "power"
        case _ => sys.error("Unknown output config kind: " + x)
      }
      tmp.setString("kind", kind)
      t.setTag(name + "_" + n, tmp)
    }
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    map.clear()
    for (i <- 0 until slots) {
      if (t.hasKey(name + "_" + i)) {
        val cfg = t.getCompoundTag(name + "_" + i)
        val cfgObj = cfg.getString("kind") match {
          case "power" => new OutputConfigPower
          case x => new OutputConfigInvalid
        }
        cfgObj.read(cfg)
        map += i -> cfgObj
      }
    }
  }

  def updated() = parent.dataSlotChanged(this)
}

object DataSlotOutputConfig {

  import scala.language.implicitConversions

  implicit def dsbfm2map(v: DataSlotOutputConfig) = v.map
}
