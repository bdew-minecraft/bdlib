/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.util.text.ITextComponent

class ButtonWidgetPressable(clicked: WidgetButton => Unit) extends Button.IPressable {
  override def onPress(b: Button): Unit = clicked(b.asInstanceOf[WidgetButton])
}

class WidgetButton(val rect: Rect, text: ITextComponent, clicked: WidgetButton => Unit)
  extends Button(rect.x.round, rect.y.round, rect.w.round, rect.h.round, text, new ButtonWidgetPressable(clicked)) with Widget {

  override def mouseClicked(p: Point, button: Int): Boolean = {
    val pp = p + rect.origin
    if (super.mouseClicked(pp.x, pp.y, button)) {
      clicked(this)
      true
    } else false
  }

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    render(m, mouse.x.round, mouse.y.round, partial)
  }
}
