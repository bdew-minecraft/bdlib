/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.gui

import java.text.DecimalFormat

import net.bdew.lib.Misc
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetDynLabel}
import net.bdew.lib.gui.{Color, Point}
import net.bdew.lib.multiblock.data.{MsgOutputCfgPower, OutputConfigPower, RSMode}
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.NetHandler

class WidgetPowerOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigPower]
  val dec = new DecimalFormat("#,##0")
  val bt = add(new WidgetButtonIcon(Point(56, 1), clicked, te.resources.btBase, te.resources.btHover))
  add(new WidgetDynLabel("%s %s/t".format(dec.format(cfg.avg), cfg.unit), 1, 5, Color.darkgray))

  val icons = Map(
    RSMode.ALWAYS -> te.resources.btEnabled,
    RSMode.NEVER -> te.resources.btDisabled,
    RSMode.RS_ON -> te.resources.btRsOn,
    RSMode.RS_OFF -> te.resources.btRsOff
  )

  val next = Map(
    RSMode.ALWAYS -> RSMode.RS_ON,
    RSMode.RS_ON -> RSMode.RS_OFF,
    RSMode.RS_OFF -> RSMode.NEVER,
    RSMode.NEVER -> RSMode.ALWAYS
  )

  override def draw(mouse: Point) {
    bt.icon = icons(cfg.rsMode)
    bt.hover = Misc.toLocal("bdlib.rsmode." + cfg.rsMode.toString.toLowerCase)
    super.draw(mouse)
  }

  def clicked(b: WidgetButtonIcon) {
    NetHandler.sendToServer(MsgOutputCfgPower(output, next(cfg.rsMode)))
  }
}
