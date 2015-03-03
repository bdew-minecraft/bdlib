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
import net.bdew.lib.Client
import net.bdew.lib.gui.{IconWrapper, Texture}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

object SensorRegistry {
  val map = collection.mutable.Map.empty[String, SensorType]
  def get(uid: String) = map.get(uid)

  def register[T <: SensorType](s: T): T = {
    if (map.isDefinedAt(s.uid))
      sys.error("Duplicate sensor ID: %s (%s / %s)".format(s.uid, map(s.uid), s))
    map += s.uid -> s
    s
  }

  register(InvalidSensor)
}

object InvalidParameter extends SensorParameter {
  override val uid = "invalid"

  @SideOnly(Side.CLIENT)
  override lazy val texture = new IconWrapper(Texture.BLOCKS, Client.blockMissingIcon)
}

object InvalidSensor extends SensorType {
  override def uid = "invalid"
  override def defaultParameter = InvalidParameter
  override def loadParameter(tag: NBTTagCompound) = InvalidParameter
  override def saveParameter(p: SensorParameter, tag: NBTTagCompound) = {}
  override def isActive(param: SensorParameter, te: TileEntity) = false
  override def paramClicked(current: SensorParameter, item: ItemStack, button: Int, mod: Int) = InvalidParameter

  @SideOnly(Side.CLIENT)
  override lazy val texture = new IconWrapper(Texture.BLOCKS, Client.blockMissingIcon)
}

object InvalidSensorPair extends SensorPair(InvalidSensor, InvalidParameter)


