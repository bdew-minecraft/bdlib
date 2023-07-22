package net.bdew.lib.multiblock.gui

import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{BaseRect, ModelDrawHelper, Point, Rect, Texture}
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import java.util.Locale
import scala.collection.mutable.ArrayBuffer

class WidgetOutputIcon(p: Point, te: CIOutputFaces, output: Int) extends Widget {
  val rect = new Rect(p, 16, 16)
  val drawRect: BaseRect[Float] = Rect(p.x + 1, p.y + 1, 14, 14)

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    val faces = te.outputFaces.inverted
    faces.get(output).map { bf =>
      ModelDrawHelper.renderWorldBlockIntoGUI(graphics, te.getLevel, bf.pos, bf.face, drawRect)
    } getOrElse {
      parent.drawTexture(graphics, rect, Texture(te.resources.disabled), te.resources.outputColors(output))
    }
  }

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    val faces = te.outputFaces.inverted
    tip += Text.translate(te.resources.unlocalizedOutputName(output))
    if (faces.isDefinedAt(output)) {
      val bf = faces(output)
      tip += te.getLevel.getBlockState(bf.target).getBlock.getName
      tip += Text.translate("bdlib.multiblock.tip.output.pos", bf.x.toString, bf.y.toString, bf.z.toString,
        Text.translate("bdlib.multiblock.face." + bf.face.toString.toLowerCase(Locale.US)))
    } else {
      tip += Text.translate("bdlib.multiblock.disabled")
    }
  }
}
