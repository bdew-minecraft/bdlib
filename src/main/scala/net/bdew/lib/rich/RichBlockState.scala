package net.bdew.lib.rich

import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

class RichBlockState(val v: BlockState) extends AnyVal {
  def withProperties[T <: Comparable[T]](vals: Iterable[(Property[T], T)]): BlockState = {
    vals.foldLeft(v) {
      case (state, (prop, value)) => state.setValue(prop, value)
    }
  }
}
