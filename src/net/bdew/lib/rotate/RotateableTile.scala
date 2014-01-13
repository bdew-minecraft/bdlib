/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.rotate

import net.bdew.lib.data.base.{UpdateKind, TileDataSlots}
import net.bdew.lib.data.DataSlotDirection

trait RotateableTile extends TileDataSlots {
  var rotation = DataSlotDirection("rotation", this).setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)
}
