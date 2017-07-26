/*
 * Copyright (c) bdew, 2013 - 2017
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
import net.bdew.lib.gui.{ModelDrawHelper, Point, Rect, Texture}
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

import scala.collection.mutable

class WidgetOutputIcon(p: Point, te: CIOutputFaces, output: Int) extends Widget {
  val rect = new Rect(p, 16, 16)
  val drawRect = Rect(p.x + 1, p.y + 1, 14, 14)

  override def draw(mouse: Point, partial: Float) {
    val faces = te.outputFaces.inverted
    faces.get(output).map { bf =>
      //noinspection UnitInMap
      ModelDrawHelper.renderWorldBlockIntoGUI(te.getWorld, bf.pos, bf.face, drawRect)
    } getOrElse {
      parent.drawTexture(rect, Texture(te.resources.disabled), te.resources.outputColors(output))
    }
  }

  def getSafeBlockName(world: World, pos: BlockPos) = {
    val state = world.getBlockState(pos)
    Option(state.getBlock.getItem(te.getWorld, pos, state))
      .map(_.getDisplayName)
      .getOrElse(state.getBlock.getLocalizedName)
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    val faces = te.outputFaces.inverted
    tip += Misc.toLocal(te.resources.unlocalizedOutputName(output))
    if (faces.isDefinedAt(output)) {
      val bf = faces(output)
      tip += getSafeBlockName(te.getWorld, bf.target)
      tip += "%d, %d, %d - %s".format(bf.x, bf.y, bf.z, Misc.toLocal("bdlib.multiblock.face." + bf.face.toString.toLowerCase(Locale.US)))
    } else {
      tip += Misc.toLocal("bdlib.multiblock.disabled")
    }
  }
}
