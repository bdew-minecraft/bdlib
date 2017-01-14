/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.DataSlotOption
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.items.ItemUtils
import net.minecraft.item.ItemStack

abstract class TileItemProcessor extends TileBaseProcessor {
  val output = DataSlotOption[ItemStack]("output", this).setUpdate(UpdateKind.SAVE)
  val outputSlots: Seq[Int]

  def isWorking = output.isDefined

  def tryFinish(): Boolean = {
    output foreach { stack =>
      val left = ItemUtils.addStackToSlots(stack, this, outputSlots, false)
      if (left.isEmpty)
        output.unset()
      else
        output.set(left)
    }
    return output.isEmpty
  }
}
