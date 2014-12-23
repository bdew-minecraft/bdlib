/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.minecraft.inventory.{IInventory, Slot}
import org.lwjgl.opengl.GL11

class BaseSlot(inv: IInventory, slot: Int, x: Int, y: Int) extends Slot(inv, slot, x, y) {
  override def getBackgroundIconIndex = {
    // Workaround for bug in GuiContainer, color isn't reset if previous slot had an item with color.
    GL11.glColor4d(1, 1, 1, 1)
    super.getBackgroundIconIndex
  }
}
