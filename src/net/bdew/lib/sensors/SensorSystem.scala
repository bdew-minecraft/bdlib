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

  abstract class SensorParameter extends GenericSensorParameter(this)

  abstract class SimpleSensor extends SimpleGenericSensor(this)

  abstract class SimpleParameter extends SimpleGenericParameter(this)

  object DisabledParameter extends SimpleParameter {
    override val uid = "disabled"

    @SideOnly(Side.CLIENT)
    override def texture = disabledTexture
  }

  object DisabledSensor extends SimpleSensor {
    override def parameters = Vector(DisabledParameter)
    override def uid = "disabled"

    override def paramClicked(current: GenericSensorParameter, item: ItemStack, button: Int, mod: Int, obj: T): GenericSensorParameter = DisabledParameter
    override def getResult(param: GenericSensorParameter, obj: T): R = defaultResult

    @SideOnly(Side.CLIENT)
    override def texture = disabledTexture
  }

  object DisabledSensorPair extends SensorPair(DisabledSensor, DisabledParameter)

  def init(): Unit = {
    BdLib.logDebug("Initialized sensor system %s for mod %s", this.getClass.getName, Misc.getActiveModId)
  }
}






