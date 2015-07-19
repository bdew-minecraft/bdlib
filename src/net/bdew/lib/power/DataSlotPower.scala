/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import net.bdew.lib.Misc
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.machine.PoweredMachine
import net.minecraft.nbt.NBTTagCompound

case class DataSlotPower(name: String, parent: DataSlotContainer) extends DataSlot {
  updateKind = Set(UpdateKind.GUI, UpdateKind.SAVE)

  var stored = 0F
  var capacity = 0F
  var maxReceive = 0F

  def inject(v: Float, simulate: Boolean): Float = {
    val canAdd = Misc.clamp(v, 0F, Misc.min(capacity - stored, maxReceive))
    if ((canAdd > 0) && !simulate) {
      stored += canAdd
      parent.dataSlotChanged(this)
    }
    return canAdd
  }

  def extract(v: Float, simulate: Boolean): Float = {
    val canExtract = Misc.clamp(v, 0F, stored)
    if ((canExtract > 0) && !simulate) {
      stored -= canExtract
      if (stored < 1) stored = 0
      parent.dataSlotChanged(this)
    }
    return canExtract
  }

  def configure(cfg: PoweredMachine) {
    capacity = cfg.maxStoredEnergy
    maxReceive = cfg.maxReceivedEnergy
  }

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val tag = new NBTTagCompound()
    tag.setFloat("stored", stored)
    if (kind == UpdateKind.GUI)
      tag.setFloat("capacity", capacity)
    t.setTag(name, tag)
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (t.hasKey(name)) {
      val tag = t.getCompoundTag(name)
      stored = tag.getFloat("stored")
      if (kind == UpdateKind.GUI)
        capacity = tag.getFloat("capacity")
    }
  }
}


