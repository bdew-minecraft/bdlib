package net.bdew.lib.sensors

import net.bdew.lib.Text
import net.minecraft.util.text.ITextComponent

abstract class GenericSensorParameter(system: SensorSystem[_, _]) {
  val uid: String

  def localizedName: ITextComponent = Text.translate(system.localizationPrefix + ".param." + uid)
}
