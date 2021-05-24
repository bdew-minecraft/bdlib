package net.bdew.lib.multiblock.tile

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfig
import net.minecraft.util.Direction

class TileOutputTracker[T <: OutputConfig](te: TileOutput[T], updateConfig: (T, Float) => Unit) {
  var outThisTick: Map[Direction, Float] = Map.empty[Direction, Float].withDefaultValue(0f)

  def track(side: Direction, amount: Float): Unit =
    outThisTick += side -> (outThisTick(side) + amount)

  te.serverTick.listen(() => {
    te.getCore foreach { core =>
      var updated = false
      for {
        side <- Direction.values()
        oNum <- core.outputFaces.get(BlockFace(te.getBlockPos, side))
        cfgGen <- core.outputConfig.get(oNum)
        cfg <- Misc.asInstanceOpt(cfgGen, te.outputConfigType)
      } {
        updateConfig(cfg, outThisTick(side))
        updated = true
      }
      if (updated) {
        core.outputConfig.updated()
        outThisTick = outThisTick.empty
      }
    }
  })
}
