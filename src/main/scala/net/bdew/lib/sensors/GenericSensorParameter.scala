package net.bdew.lib.sensors

import net.bdew.lib.Text
import net.minecraft.network.chat.Component

abstract class GenericSensorParameter(system: SensorSystem[_, _]) {
  val uid: String

  def localizedName: Component = Text.translate(system.localizationPrefix + ".param." + uid)
}
