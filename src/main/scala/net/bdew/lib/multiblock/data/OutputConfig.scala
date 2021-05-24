package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.MsgOutputCfgPayload
import net.minecraft.nbt.CompoundNBT
import sun.reflect.generics.reflectiveObjects.NotImplementedException

abstract class OutputConfig {
  def id: String
  def read(t: CompoundNBT): Unit
  def write(t: CompoundNBT): Unit
  def handleConfigPacket(m: MsgOutputCfgPayload): Unit
}

class OutputConfigInvalid extends OutputConfig {
  override val id: String = "invalid"
  def read(t: CompoundNBT): Unit = {}
  def write(t: CompoundNBT): Unit = throw new NotImplementedException
  def handleConfigPacket(m: MsgOutputCfgPayload): Unit = throw new NotImplementedException
}

object OutputConfigManager {
  var loaders = Map.empty[String, () => OutputConfig]
  def register(id: String, loader: () => OutputConfig): Unit = loaders += id -> loader
  def create(id: String): OutputConfig = loaders.get(id).map(_.apply()).getOrElse(new OutputConfigInvalid)

  register("fluid", () => new OutputConfigFluid)
  register("power", () => new OutputConfigPower)
  register("items", () => new OutputConfigItems)
}