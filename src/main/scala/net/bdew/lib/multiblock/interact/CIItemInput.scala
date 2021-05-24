package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.tile.TileController
import net.minecraftforge.items.IItemHandler

trait CIItemInput extends TileController {
  def itemInput: IItemHandler
}
