package net.bdew.lib.rotate

import net.bdew.lib.data.DataSlotDirection
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}

trait RotatableTile extends TileDataSlots {
  val rotation: DataSlotDirection = DataSlotDirection("rotation", this).setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER)
}
