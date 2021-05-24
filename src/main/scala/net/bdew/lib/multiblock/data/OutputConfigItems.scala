/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.network.{MsgOutputCfgPayload, MsgOutputCfgRSMode}
import net.minecraft.nbt.CompoundNBT

class OutputConfigItems extends OutputConfig with OutputConfigRSControllable {
  override def id = "items"
  var rsMode: RSMode.Value = RSMode.ALWAYS

  def read(t: CompoundNBT): Unit = {
    rsMode = RSMode(t.getInt("rsMode"))
  }

  def write(t: CompoundNBT): Unit = {
    t.putInt("rsMode", rsMode.id)
  }

  def handleConfigPacket(m: MsgOutputCfgPayload): Unit = m match {
    case MsgOutputCfgRSMode(r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }

}
