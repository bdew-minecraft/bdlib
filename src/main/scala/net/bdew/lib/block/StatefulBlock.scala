package net.bdew.lib.block

import net.minecraft.block.{AbstractBlock, Block, BlockState}

class StatefulBlock(props: AbstractBlock.Properties) extends Block(props) {
  registerDefaultState(getDefaultState(defaultBlockState()))

  def getDefaultState(base: BlockState): BlockState = base
}
