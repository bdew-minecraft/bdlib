package net.bdew.lib.sensors

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.Text
import net.bdew.lib.gui.{DrawTarget, Rect}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class GenericSensorType[-T, +R](system: SensorSystem[T, R]) {
  def uid: String

  system.register(this)

  def getTooltip(obj: T): List[Component] = List(localizedName)
  def getParamTooltip(obj: T, param: GenericSensorParameter): List[Component] = List(param.localizedName)

  def localizedName: Component = Text.translate(system.localizationPrefix + "." + uid)

  def defaultParameter: GenericSensorParameter

  def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: T): GenericSensorParameter

  def saveParameter(p: GenericSensorParameter, tag: CompoundTag): Unit
  def loadParameter(tag: CompoundTag): GenericSensorParameter
  def isValidParameter(p: GenericSensorParameter, obj: T): Boolean

  def getResult(param: GenericSensorParameter, obj: T): R

  @OnlyIn(Dist.CLIENT)
  def drawSensor(m: PoseStack, rect: Rect, target: DrawTarget, obj: T): Unit

  @OnlyIn(Dist.CLIENT)
  def drawParameter(m: PoseStack, rect: Rect, target: DrawTarget, obj: T, param: GenericSensorParameter): Unit
}
