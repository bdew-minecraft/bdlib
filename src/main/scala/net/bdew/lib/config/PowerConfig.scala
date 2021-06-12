package net.bdew.lib.config

import net.minecraftforge.common.ForgeConfigSpec

class PowerConfig(spec: ForgeConfigSpec.Builder, defMaxReceive: Float, defCapacity: Float) extends ConfigSection {
  val maxReceive: () => Float = floatVal(spec, "MaxInput", "Maximum energy input (FE/t)", defMaxReceive, 0)
  val capacity: () => Float = floatVal(spec, "Capacity", "Energy capacity (FE)", defCapacity, 0)
}