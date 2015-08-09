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
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.render.connected.BlockAdditionalRender

import scala.collection.mutable

class WidgetOutputIcon(p: Point, te: CIOutputFaces, output: Int) extends Widget {
  val rect = new Rect(p, 16, 16)

  override def draw(mouse: Point) {
    val faces = te.outputFaces.inverted
    if (faces.isDefinedAt(output)) {
      val bf = faces(output)
      bf.origin.block(te.getWorldObj) foreach { block =>
        parent.drawTexture(rect, Texture(Texture.BLOCKS, block.getIcon(te.getWorldObj, bf.origin.x, bf.origin.y, bf.origin.z, bf.face.ordinal())))
        if (block.isInstanceOf[BlockAdditionalRender]) {
          for (over <- block.asInstanceOf[BlockAdditionalRender].getFaceOverlays(te.getWorldObj, bf.origin.x, bf.origin.y, bf.origin.z, bf.face))
            parent.drawTexture(rect, Texture(Texture.BLOCKS, over.icon), over.color)
        }
      }
    } else {
      parent.drawTexture(rect, Texture(Texture.BLOCKS, te.resources.disabled), te.resources.outputColors(output))
    }
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    val faces = te.outputFaces.inverted
    tip += Misc.toLocal(te.resources.unlocalizedOutputName(output))
    if (faces.isDefinedAt(output)) {
      val bf = faces(output)
      bf.origin.block(te.getWorldObj) map { block =>
        tip += block.getLocalizedName
        tip += "%d, %d, %d - %s".format(bf.x, bf.y, bf.z, Misc.toLocal("bdlib.multiblock.face." + bf.face.toString.toLowerCase(Locale.US)))
      }
    } else {
      tip += Misc.toLocal("bdlib.multiblock.disabled")
    }
  }
}
