/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.gui

import java.util.Locale

import net.bdew.lib.Misc
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubContainer}
import net.bdew.lib.gui.{Point, Rect}
import net.bdew.lib.multiblock.data.{MsgOutputCfgRSMode, OutputConfigRSControllable, RSMode}
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.NetHandler

class WidgetRSConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubContainer(new Rect(p, 16, 16)) {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigRSControllable]

  val bt = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover))

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
    bt.hover = Misc.toLocal("bdlib.rsmode." + cfg.rsMode.toString.toLowerCase(Locale.US))
    super.draw(mouse)
  }

  def clicked(b: WidgetButtonIcon) {
    NetHandler.sendToServer(MsgOutputCfgRSMode(output, next(cfg.rsMode)))
  }
}
