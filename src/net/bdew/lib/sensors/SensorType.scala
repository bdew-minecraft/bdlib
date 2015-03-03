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
import net.bdew.lib.gui.Texture
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

abstract class SensorType {
  def uid: String

  def localizedName = Misc.toLocal("bdew.sensor." + uid)

  def defaultParameter: SensorParameter
  def paramClicked(current: SensorParameter, item: ItemStack, button: Int, mod: Int): SensorParameter
  def saveParameter(p: SensorParameter, tag: NBTTagCompound)
  def loadParameter(tag: NBTTagCompound): SensorParameter

  def isActive(param: SensorParameter, te: TileEntity): Boolean

  @SideOnly(Side.CLIENT)
  def texture: Texture
}
