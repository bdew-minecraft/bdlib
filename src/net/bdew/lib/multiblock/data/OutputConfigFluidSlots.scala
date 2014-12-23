/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.{MsgOutputCfg, MsgOutputCfgSlot}
import net.minecraft.nbt.NBTTagCompound

trait OutputConfigSlots {
  val slotsDef: SlotSet
  var slot = slotsDef.default
}

class OutputConfigFluidSlots(val slotsDef: SlotSet) extends OutputConfigFluid with OutputConfigSlots {
  override def id = slotsDef.outputConfigId
  override def read(t: NBTTagCompound) {
    super.read(t)
    slot = slotsDef.get(t.getString("slot"))
  }
  override def write(t: NBTTagCompound) {
    super.write(t)
    t.setString("slot", slot.id)
  }
  override def handleConfigPacket(m: MsgOutputCfg) = m match {
    case MsgOutputCfgSlot(n, id) => slot = slotsDef.get(id)
    case _ => super.handleConfigPacket(m)
  }
}
