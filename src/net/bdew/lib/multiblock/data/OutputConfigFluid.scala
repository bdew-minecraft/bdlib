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
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

import scala.collection.mutable

class OutputConfigFluid extends OutputConfig with OutputConfigRSControllable {
  override def id = "fluid"
  val values = mutable.Queue.empty[Double]
  var rsMode = RSMode.ALWAYS

  final val ticks = 20

  import net.bdew.lib.nbt._

  def avg = values.sum / values.size

  def updateAvg(v: Double) {
    values += v
    if (values.size > ticks)
      values.dequeue()
  }

  def read(t: NBTTagCompound) {
    values.clear()
    values ++= t.getList[Double]("values")
    rsMode = RSMode(t.getInteger("rsMode"))
  }

  def write(t: NBTTagCompound) {
    val l = new NBTTagList
    t.setList("values", values)
    t.setInteger("rsMode", rsMode.id)
  }

  def handleConfigPacket(m: MsgOutputCfg) = m match {
    case MsgOutputCfgRSMode(_, r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }
}
