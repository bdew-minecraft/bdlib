/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.minecraft.inventory.container.ClickType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
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

  override def saveParameter(p: GenericSensorParameter, tag: CompoundNBT): Unit = tag.putString("param", p.uid)
  override def loadParameter(tag: CompoundNBT): GenericSensorParameter = parameterMap.getOrElse(tag.getString("param"), system.DisabledParameter)
  override def isValidParameter(p: GenericSensorParameter, obj: T): Boolean = parameters.contains(p)

  @OnlyIn(Dist.CLIENT)
  def texture: Texture

  @OnlyIn(Dist.CLIENT)
  def textureColor: Color = Color.white

  @OnlyIn(Dist.CLIENT)
  override def drawSensor(m: MatrixStack, rect: Rect, target: DrawTarget, obj: T): Unit = {
    target.drawTexture(m, rect, texture, textureColor)
  }

  @OnlyIn(Dist.CLIENT)
  override def drawParameter(m: MatrixStack, rect: Rect, target: DrawTarget, obj: T, param: GenericSensorParameter): Unit = {
    param match {
      case x: SimpleGenericParameter => target.drawTexture(m, rect, x.texture, x.textureColor)
      case _ => target.drawTexture(m, rect, system.DisabledParameter.texture, system.DisabledParameter.textureColor)
    }
  }
}

