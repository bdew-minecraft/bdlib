/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.MsgOutputCfg
import net.minecraft.nbt.NBTTagCompound
import sun.reflect.generics.reflectiveObjects.NotImplementedException

abstract class OutputConfig {
  def id: String
  def read(t: NBTTagCompound)
  def write(t: NBTTagCompound)
  def handleConfigPacket(m: MsgOutputCfg)
}

class OutputConfigInvalid extends OutputConfig {
  override val id: String = "invalid"
  def read(t: NBTTagCompound) {}
  def write(t: NBTTagCompound) = throw new NotImplementedException
  def handleConfigPacket(m: MsgOutputCfg) = throw new NotImplementedException
}

object OutputConfigManager {
  var loaders = Map.empty[String, () => OutputConfig]
  def register(id: String, loader: () => OutputConfig) = loaders += id -> loader
  def create(id: String) = loaders.get(id).map(_.apply()).getOrElse(new OutputConfigInvalid)

  register("fluid", () => new OutputConfigFluid)
  register("power", () => new OutputConfigPower)
}