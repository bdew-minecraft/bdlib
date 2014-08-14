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

case class MsgOutputCfgPower(output: Int, rsMode: RSMode.Value) extends MsgOutputCfg

class OutputConfigPower extends OutputConfig {
  var avg = 0.0
  var rsMode = RSMode.ALWAYS
  var unit = "MJ"

  final val decay = 0.5

  def updateAvg(v: Double) {
    avg = avg * decay + (1 - decay) * v
  }

  def read(t: NBTTagCompound) {
    avg = t.getDouble("avg")
    rsMode = RSMode(t.getInteger("rsMode"))
    unit = t.getString("unit")
  }

  def write(t: NBTTagCompound) {
    t.setDouble("avg", avg)
    t.setInteger("rsMode", rsMode.id)
    t.setString("unit", unit)
  }

  def handleConfigPacket(m: MsgOutputCfg) = m match {
    case MsgOutputCfgPower(_, r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }
}
