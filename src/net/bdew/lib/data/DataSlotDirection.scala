/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.DataSlotContainer
import net.bdew.lib.data.mixins.DataSlotNBT
import net.bdew.lib.nbt.converters.TEnumFacing
import net.minecraft.util.EnumFacing

case class DataSlotDirection(name: String, parent: DataSlotContainer, default: EnumFacing = EnumFacing.UP) extends DataSlotNBT[EnumFacing] {
  override def nbtType = TEnumFacing
}
