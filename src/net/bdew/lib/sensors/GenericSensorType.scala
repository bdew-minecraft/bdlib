/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc
import net.bdew.lib.gui.{Color, DrawTarget, Rect, Texture}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

abstract class GenericSensorType[-T, +R](system: SensorSystem[T, R]) {
  def uid: String

  system.register(this)

  def localizedName = Misc.toLocal(system.localizationPrefix + "." + uid)

  def defaultParameter: GenericSensorParameter
  def paramClicked(current: GenericSensorParameter, item: ItemStack, button: Int, mod: Int): GenericSensorParameter
  def saveParameter(p: GenericSensorParameter, tag: NBTTagCompound)
  def loadParameter(tag: NBTTagCompound): GenericSensorParameter

  def getResult(param: GenericSensorParameter, obj: T): R

  @SideOnly(Side.CLIENT)
  def drawSensor(rect: Rect, target: DrawTarget): Unit = {
    target.drawTexture(rect, texture, textureColor)
  }

  @SideOnly(Side.CLIENT)
  def texture: Texture

  @SideOnly(Side.CLIENT)
  def textureColor = Color.white
}
