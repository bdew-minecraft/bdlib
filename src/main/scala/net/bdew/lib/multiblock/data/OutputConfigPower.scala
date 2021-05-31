package net.bdew.lib.multiblock.data

import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.network.{MsgOutputCfg, MsgOutputCfgRSMode}
import net.minecraft.nbt.CompoundNBT

class OutputConfigPower(var unit: String = "fe") extends OutputConfig with OutputConfigRSControllable {
  override val id = "power"
  var avg = 0.0
  var rsMode: RSMode.Value = RSMode.ALWAYS

  final val decay = 0.5

  def updateAvg(v: Double): Unit = {
    avg = avg * decay + (1 - decay) * v
  }

  def read(t: CompoundNBT): Unit = {
    avg = t.getDouble("avg")
    rsMode = RSMode(t.getInt("rsMode"))
    unit = t.getString("unit")
  }

  def write(t: CompoundNBT): Unit = {
    t.putDouble("avg", avg)
    t.putInt("rsMode", rsMode.id)
    t.putString("unit", unit)
  }

  def handleConfigPacket(m: MsgOutputCfg): Unit = m match {
    case MsgOutputCfgRSMode(_, r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }
}
