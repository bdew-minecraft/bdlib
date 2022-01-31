package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.core.Direction

trait MIOutput[T <: OutputConfig] extends TileModule {
  val outputConfigType: Class[T]
  type OCType = T

  def doOutput(face: Direction, cfg: OCType): Unit

  def makeCfgObject(face: Direction): OutputConfig
}
