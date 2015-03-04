/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.Misc
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

abstract class SimpleSensor extends SensorType {
  def parameters: IndexedSeq[SensorParameter]

  override lazy val defaultParameter = parameters.headOption.getOrElse(InvalidParameter)

  lazy val parameterMap = (parameters map (x => x.uid -> x)).toMap

  override def paramClicked(current: SensorParameter, item: ItemStack, button: Int, mod: Int) =
    if (item == null && button == 0 && mod == 0)
      Misc.nextInSeq(parameters, current)
    else if (item == null && button == 1 && mod == 0)
      Misc.prevInSeq(parameters, current)
    else
      current

  override def saveParameter(p: SensorParameter, tag: NBTTagCompound) = tag.setString("param", p.uid)
  override def loadParameter(tag: NBTTagCompound) = parameterMap.getOrElse(tag.getString("param"), InvalidParameter)
}
