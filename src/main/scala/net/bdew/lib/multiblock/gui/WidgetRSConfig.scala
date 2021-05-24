package net.bdew.lib.multiblock.gui

import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubContainer}
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.data.OutputConfigRSControllable
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.{MsgOutputCfg, MsgOutputCfgRSMode, MultiblockNetHandler}
import net.minecraft.util.text.ITextComponent

import java.util.Locale

class WidgetRSConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubContainer(new Rect(p, 16, 16)) {
  def cfg: OutputConfigRSControllable = te.outputConfig(output).asInstanceOf[OutputConfigRSControllable]

  val bt: WidgetButtonIcon = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover) {
    override def icon: Texture = icons(cfg.rsMode)
    override def hover: ITextComponent = Text.translate("bdlib.rsmode." + cfg.rsMode.toString.toLowerCase(Locale.US))
  })

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

  def clicked(b: WidgetButtonIcon): Unit = {
    MultiblockNetHandler.sendToServer(MsgOutputCfg(output, MsgOutputCfgRSMode(next(cfg.rsMode))))
  }
}
