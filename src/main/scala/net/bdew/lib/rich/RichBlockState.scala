/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.minecraft.block.BlockState
import net.minecraft.state.Property

class RichBlockState(val v: BlockState) extends AnyVal {
  def withProperties[T <: Comparable[T]](vals: Iterable[(Property[T], T)]): BlockState = {
    vals.foldLeft(v) {
      case (state, (prop, value)) => state.setValue(prop, value)
    }
  }
}
