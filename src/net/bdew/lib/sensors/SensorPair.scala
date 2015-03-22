/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

case class SensorPair[-T, +R](sensor: GenericSensorType[T, R], param: GenericSensorParameter) {
  def getResult(obj: T) = sensor.getResult(param, obj)
}
