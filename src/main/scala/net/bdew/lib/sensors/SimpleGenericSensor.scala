package net.bdew.lib.sensors

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class SimpleGenericParameter(system: SensorSystem[_, _]) extends GenericSensorParameter(system) {
  @OnlyIn(Dist.CLIENT)
  def texture: Texture

  @OnlyIn(Dist.CLIENT)
  def textureColor: Color = Color.white
}

abstract class SimpleGenericSensor[-T, +R](system: SensorSystem[T, R]) extends GenericSensorType[T, R](system) {
  def parameters: IndexedSeq[GenericSensorParameter]

  override lazy val defaultParameter: GenericSensorParameter = parameters.headOption.getOrElse(system.DisabledParameter)

  lazy val parameterMap: Map[String, GenericSensorParameter] = (parameters map (x => x.uid -> x)).toMap

  override def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: T): GenericSensorParameter =
    if (item.isEmpty && button == 0 && clickType == ClickType.PICKUP)
      Misc.nextInSeq(parameters, current)
    else if (item.isEmpty && button == 1 && clickType == ClickType.PICKUP)
      Misc.prevInSeq(parameters, current)
    else
      current

  override def saveParameter(p: GenericSensorParameter, tag: CompoundTag): Unit = tag.putString("param", p.uid)
  override def loadParameter(tag: CompoundTag): GenericSensorParameter = parameterMap.getOrElse(tag.getString("param"), system.DisabledParameter)
  override def isValidParameter(p: GenericSensorParameter, obj: T): Boolean = parameters.contains(p)

  @OnlyIn(Dist.CLIENT)
  def texture: Texture

  @OnlyIn(Dist.CLIENT)
  def textureColor: Color = Color.white

  @OnlyIn(Dist.CLIENT)
  override def drawSensor(graphics: GuiGraphics, rect: Rect, target: DrawTarget, obj: T): Unit = {
    target.drawTexture(graphics, rect, texture, textureColor)
  }

  @OnlyIn(Dist.CLIENT)
  override def drawParameter(graphics: GuiGraphics, rect: Rect, target: DrawTarget, obj: T, param: GenericSensorParameter): Unit = {
    param match {
      case x: SimpleGenericParameter => target.drawTexture(graphics, rect, x.texture, x.textureColor)
      case _ => target.drawTexture(graphics, rect, system.DisabledParameter.texture, system.DisabledParameter.textureColor)
    }
  }
}

