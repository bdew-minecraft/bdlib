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
import net.bdew.lib.gui._
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

abstract class SensorSystem[T, R](defaultResult: R) {
  val map = collection.mutable.Map.empty[String, GenericSensorType[T, R]]
  def get(uid: String) = map.get(uid)

  def register[Z <: GenericSensorType[T, R]](s: Z): Z = {
    if (map.isDefinedAt(s.uid))
      sys.error("Duplicate sensor ID: %s (%s / %s)".format(s.uid, map(s.uid), s))
    map += s.uid -> s
    s
  }

  @SideOnly(Side.CLIENT)
  def disabledTexture: Texture

  def localizationPrefix: String

  @SideOnly(Side.CLIENT)
  def drawResult(result: R, rect: Rect, target: DrawTarget): Unit

  @SideOnly(Side.CLIENT)
  def getLocalizedResultText(result: R): String

  abstract class SensorType extends GenericSensorType(this)

  abstract class SimpleSensor extends SimpleGenericSensor(this)

  abstract class SensorParameter extends GenericSensorParameter(this)

  object DisabledParameter extends SensorParameter {
    override val uid = "disabled"

    @SideOnly(Side.CLIENT)
    override val texture = disabledTexture
  }

  object DisabledSensor extends SensorType {
    override def uid = "disabled"
    override def defaultParameter = DisabledParameter
    override def loadParameter(tag: NBTTagCompound) = DisabledParameter
    override def saveParameter(p: GenericSensorParameter, tag: NBTTagCompound) = {}
    override def getResult(param: GenericSensorParameter, obj: T) = defaultResult
    override def paramClicked(current: GenericSensorParameter, item: ItemStack, button: Int, mod: Int) = DisabledParameter

    @SideOnly(Side.CLIENT)
    override val texture = disabledTexture
  }

  object DisabledSensorPair extends SensorPair(DisabledSensor, DisabledParameter)

  def init(): Unit = {
    BdLib.logDebug("Initialized sensor system %s for mod %s", this.getClass.getName, Misc.getActiveModId)
  }
}






