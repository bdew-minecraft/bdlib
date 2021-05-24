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
