package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.{MsgOutputCfg, MsgOutputCfgSlot}
import net.minecraft.nbt.CompoundTag

trait OutputConfigSlots {
  val slotsDef: SlotSet
  var slot: slotsDef.Slot = slotsDef.default
}

class OutputConfigFluidSlots(val slotsDef: SlotSet) extends OutputConfigFluid with OutputConfigSlots {
  override def id: String = slotsDef.outputConfigId
  override def read(t: CompoundTag): Unit = {
    super.read(t)
    slot = slotsDef.get(t.getString("slot"))
  }
  override def write(t: CompoundTag): Unit = {
    super.write(t)
    t.putString("slot", slot.id)
  }
  override def handleConfigPacket(m: MsgOutputCfg): Unit = m match {
    case MsgOutputCfgSlot(_, id) => slot = slotsDef.get(id)
    case _ => super.handleConfigPacket(m)
  }
}
