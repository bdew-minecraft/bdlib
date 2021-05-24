package net.bdew.lib.multiblock.block

import net.bdew.lib.multiblock.tile.TileOutput

trait BlockOutput[T <: TileOutput[_]] extends BlockModule[T]
