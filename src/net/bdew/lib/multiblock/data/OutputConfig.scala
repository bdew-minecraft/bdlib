/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.MsgOutputCfg
import net.minecraft.nbt.NBTTagCompound
import sun.reflect.generics.reflectiveObjects.NotImplementedException

abstract class OutputConfig {
  def read(t: NBTTagCompound)
  def write(t: NBTTagCompound)
  def handleConfigPacket(m: MsgOutputCfg)
}

class OutputConfigInvalid extends OutputConfig {
  def read(t: NBTTagCompound) {}
  def write(t: NBTTagCompound) = throw new NotImplementedException
  def handleConfigPacket(m: MsgOutputCfg) = throw new NotImplementedException
}
