package net.bdew.lib.multiblock.data

import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.network.{MsgOutputCfgPayload, MsgOutputCfgRSMode}
import net.minecraft.nbt.CompoundNBT

class OutputConfigItems extends OutputConfig with OutputConfigRSControllable {
  override def id = "items"
  var rsMode: RSMode.Value = RSMode.ALWAYS

  def read(t: CompoundNBT): Unit = {
    rsMode = RSMode(t.getInt("rsMode"))
  }

  def write(t: CompoundNBT): Unit = {
    t.putInt("rsMode", rsMode.id)
  }

  def handleConfigPacket(m: MsgOutputCfgPayload): Unit = m match {
    case MsgOutputCfgRSMode(r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }

}
