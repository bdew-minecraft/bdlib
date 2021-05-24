/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.network.{MsgOutputCfgPayload, MsgOutputCfgRSMode}
import net.minecraft.nbt.CompoundNBT

import scala.collection.mutable

class OutputConfigFluid extends OutputConfig with OutputConfigRSControllable {
  override def id = "fluid"
  val values: mutable.Queue[Double] = mutable.Queue.empty[Double]
  var rsMode: RSMode.Value = RSMode.ALWAYS

  final val ticks = 20

  def avg: Double = if (values.nonEmpty) values.sum / values.size else 0

  def updateAvg(v: Double): Unit = {
    values += v
    if (values.size > ticks)
      values.dequeue()
  }

  def read(t: CompoundNBT): Unit = {
    values.clear()
    values ++= t.getListVals[Double]("values")
    rsMode = RSMode(t.getInt("rsMode"))
  }

  def write(t: CompoundNBT): Unit = {
    t.setListVals("values", values)
    t.putInt("rsMode", rsMode.id)
  }

  def handleConfigPacket(m: MsgOutputCfgPayload): Unit = m match {
    case MsgOutputCfgRSMode(r) => rsMode = r
    case _ => sys.error("Invalid output config packet %s to config %s".format(m, this))
  }
}
