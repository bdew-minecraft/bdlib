/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.data.base.{DataSlotContainer, UpdateKind}
import net.bdew.lib.data.mixins.DataSlotNBTOption
import net.bdew.lib.nbt.converters.TBlockPos
import net.minecraft.util.math.BlockPos

case class DataSlotPos(name: String, parent: DataSlotContainer) extends DataSlotNBTOption[BlockPos] {
  override def nbtType = TBlockPos
  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)
}
