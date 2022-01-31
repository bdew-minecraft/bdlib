package net.bdew.lib.multiblock.tile

import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.data.OutputConfigRSControllable
import net.minecraft.world.level.block.entity.BlockEntity

trait RSControllableOutput extends BlockEntity {
  def checkCanOutput(cfg: OutputConfigRSControllable): Boolean = {
    if (cfg.rsMode == RSMode.ALWAYS) return true
    if (cfg.rsMode == RSMode.NEVER) return false
    getLevel.hasNeighborSignal(getBlockPos) ^ (cfg.rsMode == RSMode.RS_OFF)
  }
}
