package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.{MsgOutputCfgPayload, MsgOutputCfgSlot}
import net.minecraft.nbt.CompoundNBT

trait OutputConfigSlots {
  val slotsDef: SlotSet
  var slot: slotsDef.Slot = slotsDef.default
}

class OutputConfigFluidSlots(val slotsDef: SlotSet) extends OutputConfigFluid with OutputConfigSlots {
  override def id: String = slotsDef.outputConfigId
  override def read(t: CompoundNBT): Unit = {
    super.read(t)
    slot = slotsDef.get(t.getString("slot"))
  }
  override def write(t: CompoundNBT): Unit = {
    super.write(t)
    t.putString("slot", slot.id)
  }
  override def handleConfigPacket(m: MsgOutputCfgPayload): Unit = m match {
    case MsgOutputCfgSlot(id) => slot = slotsDef.get(id)
    case _ => super.handleConfigPacket(m)
  }
}
