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
import net.bdew.lib.gui._
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.inventory.container.ClickType
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class SensorSystem[T, R](defaultResult: R) {
  val map = collection.mutable.Map.empty[String, GenericSensorType[T, R]]
  def get(uid: String): Option[GenericSensorType[T, R]] = map.get(uid)

  def register[Z <: GenericSensorType[T, R]](s: Z): Z = {
    if (map.isDefinedAt(s.uid))
      sys.error("Duplicate sensor ID: %s (%s / %s)".format(s.uid, map(s.uid), s))
    map += s.uid -> s
    s
  }

  @OnlyIn(Dist.CLIENT)
  def disabledTexture: Texture

  def localizationPrefix: String

  @OnlyIn(Dist.CLIENT)
  def drawResult(m: MatrixStack, result: R, rect: Rect, target: DrawTarget): Unit

  @OnlyIn(Dist.CLIENT)
  def getResultText(result: R): ITextComponent

  abstract class SensorType extends GenericSensorType(this)

  abstract class SensorParameter extends GenericSensorParameter(this)

  abstract class SimpleSensor extends SimpleGenericSensor(this)

  abstract class SimpleParameter extends SimpleGenericParameter(this)

  object DisabledParameter extends SimpleParameter {
    override val uid = "disabled"

    @OnlyIn(Dist.CLIENT)
    override def texture: Texture = disabledTexture
  }

  object DisabledSensor extends SimpleSensor {
    override def parameters = Vector(DisabledParameter)
    override def uid = "disabled"

    override def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: T): GenericSensorParameter = DisabledParameter
    override def getResult(param: GenericSensorParameter, obj: T): R = defaultResult

    @OnlyIn(Dist.CLIENT)
    override def texture: Texture = disabledTexture
  }

  object DisabledSensorPair extends SensorPair(DisabledSensor, DisabledParameter)

  def init(): Unit = {
    BdLib.logDebug("Initialized sensor system %s for mod %s", this.getClass.getName, Misc.getActiveModId)
  }
}






