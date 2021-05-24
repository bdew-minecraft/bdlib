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
import net.bdew.lib.Text
import net.bdew.lib.gui.{DrawTarget, Rect}
import net.minecraft.inventory.container.ClickType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class GenericSensorType[-T, +R](system: SensorSystem[T, R]) {
  def uid: String

  system.register(this)

  def getTooltip(obj: T): List[ITextComponent] = List(localizedName)
  def getParamTooltip(obj: T, param: GenericSensorParameter): List[ITextComponent] = List(param.localizedName)

  def localizedName: ITextComponent = Text.translate(system.localizationPrefix + "." + uid)

  def defaultParameter: GenericSensorParameter

  def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: T): GenericSensorParameter

  def saveParameter(p: GenericSensorParameter, tag: CompoundNBT): Unit
  def loadParameter(tag: CompoundNBT): GenericSensorParameter
  def isValidParameter(p: GenericSensorParameter, obj: T): Boolean

  def getResult(param: GenericSensorParameter, obj: T): R

  @OnlyIn(Dist.CLIENT)
  def drawSensor(m: MatrixStack, rect: Rect, target: DrawTarget, obj: T): Unit

  @OnlyIn(Dist.CLIENT)
  def drawParameter(m: MatrixStack, rect: Rect, target: DrawTarget, obj: T, param: GenericSensorParameter): Unit
}
