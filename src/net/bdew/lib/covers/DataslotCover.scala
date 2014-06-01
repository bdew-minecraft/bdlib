/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.covers

import net.minecraft.item.Item
import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.bdew.lib.BdLib

case class DataslotCover(name: String, parent: TileCoverable) extends DataSlotVal[Option[ItemCover]] {
  var cval: Option[ItemCover] = None

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER)

  def :=(v: ItemCover) = update(Option(v))

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) = {
    try {
      cval = Option(Item.itemsList(t.getInteger(name)).asInstanceOf[ItemCover])
    } catch {
      case t: Throwable => BdLib.logError("Failed to load cover %s for block at %d, %d, %d: %s", name, parent.xCoord, parent.yCoord, parent.zCoord, t.toString)
        t.printStackTrace()
    }
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) =
    for (v <- cval) t.setInteger(name, v.itemID)
}
