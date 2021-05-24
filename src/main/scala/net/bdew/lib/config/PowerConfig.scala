package net.bdew.lib.config

import net.minecraftforge.common.ForgeConfigSpec

class PowerConfig(spec: ForgeConfigSpec.Builder, defMaxReceive: Double, defActivation: Double, defCapacity: Double) {
  val maxReceive: ForgeConfigSpec.DoubleValue =
    spec.comment("Maximum accepted energy per tick")
      .translation("bdlib.config.power.maxinput")
      .defineInRange("MaxInput", defMaxReceive, 0D, Double.MaxValue)

  val activation: ForgeConfigSpec.DoubleValue =
    spec.comment("Minimum energy for the machine to do any work")
      .translation("bdlib.config.power.activation")
      .defineInRange("Activation", defActivation, 0D, Double.MaxValue)

  val capacity: ForgeConfigSpec.DoubleValue =
    spec.comment("Maximum stored energy")
      .translation("bdlib.config.power.capacity")
      .defineInRange("Capacity", defCapacity, 0D, Double.MaxValue)
}

object PowerConfig {
  def apply(spec: ForgeConfigSpec.Builder, name: String, comment: String, defMaxReceive: Double, defActivation: Double, defCapacity: Double): PowerConfig =
    ConfigSection(spec, name, comment, new PowerConfig(spec, defMaxReceive, defActivation, defCapacity))
}

trait PoweredMachineConfig {
  def power: PowerConfig
}