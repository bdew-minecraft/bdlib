package net.bdew.lib.rich

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraftforge.common.capabilities.Capability

import scala.reflect.ClassTag

class RichBlockReader(val v: IBlockReader) extends AnyVal {
  def getTileSafe[T: ClassTag](p: BlockPos): Option[T] = {
    val tile = v.getBlockEntity(p)
    if (tile != null && implicitly[ClassTag[T]].runtimeClass.isInstance(tile))
      Some(tile.asInstanceOf[T])
    else
      None
  }

  def getBlockSafe[T: ClassTag](p: BlockPos): Option[T] = {
    val block = v.getBlockState(p).getBlock
    if (block != null && implicitly[ClassTag[T]].runtimeClass.isInstance(block))
      Some(block.asInstanceOf[T])
    else
      None
  }

  def getCapSafe[T](p: BlockPos, side: Direction, cap: Capability[T]): Option[T] = {
    val te = v.getBlockEntity(p)
    if (v != null)
      te.getCapability(cap, side).map[Option[T]](x => Option(x)).orElseGet(() => None)
    else
      None
  }
}
