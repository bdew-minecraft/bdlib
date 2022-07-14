package net.bdew.lib.multiblock.render

import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.multiblock.block.BlockOutput
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.event.RegisterColorHandlersEvent

object MultiblockRenderHelper {
  def setupColors(ev: RegisterColorHandlersEvent.Block, blocks: Iterable[Block], resources: ResourceProvider): Unit = {
    ev.register(
      new OutputBlockColor(resources),
      blocks.filter(_.isInstanceOf[BlockOutput[_]]).toSeq: _*
    )
  }
}
