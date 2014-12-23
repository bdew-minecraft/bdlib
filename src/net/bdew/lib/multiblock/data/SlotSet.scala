/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.gui.Texture

abstract class SlotSet(val outputConfigId: String) {
  // outputConfigId should be registered in OutputConfigManager

  var slotMap = Map.empty[String, Slot]

  case class Slot(id: String, name: String, texture: Texture) {
    slotMap += id -> this
    def next = order(this)
  }

  def default: Slot
  val order: Map[Slot, Slot]

  def get(id: String) = slotMap(id)
}
