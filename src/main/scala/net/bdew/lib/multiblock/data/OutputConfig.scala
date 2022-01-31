package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.MsgOutputCfg
import net.minecraft.nbt.CompoundTag
import sun.reflect.generics.reflectiveObjects.NotImplementedException

abstract class OutputConfig {
  def id: String
  def read(t: CompoundTag): Unit
  def write(t: CompoundTag): Unit
  def handleConfigPacket(m: MsgOutputCfg): Unit
}

class OutputConfigInvalid extends OutputConfig {
  override val id: String = "invalid"
  def read(t: CompoundTag): Unit = {}
  def write(t: CompoundTag): Unit = throw new NotImplementedException
  def handleConfigPacket(m: MsgOutputCfg): Unit = throw new NotImplementedException
}

object OutputConfigManager {
  var loaders = Map.empty[String, () => OutputConfig]
  def register(id: String, loader: () => OutputConfig): Unit = loaders += id -> loader
  def create(id: String): OutputConfig = loaders.get(id).map(_.apply()).getOrElse(new OutputConfigInvalid)

  register("fluid", () => new OutputConfigFluid)
  register("power", () => new OutputConfigPower)
  register("items", () => new OutputConfigItems)
}