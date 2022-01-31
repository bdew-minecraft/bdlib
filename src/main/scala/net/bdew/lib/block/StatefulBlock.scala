package net.bdew.lib.block

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

class StatefulBlock(props: BlockBehaviour.Properties) extends Block(props) {
  registerDefaultState(getDefaultState(defaultBlockState()))

  def getDefaultState(base: BlockState): BlockState = base
}
