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

class OutputConfigItems extends OutputConfig with OutputConfigRSControllable {
  override def id = "items"
  var rsMode = RSMode.ALWAYS

  def read(t: NBTTagCompound) {
    rsMode = RSMode(t.getInteger("rsMode"))
  }

  def write(t: NBTTagCompound) {
    t.setInteger("rsMode", rsMode.id)
  }

  def handleConfigPacket(m: MsgOutputCfg) = m match {
    case MsgOutputCfgRSMode(_, r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }

}
