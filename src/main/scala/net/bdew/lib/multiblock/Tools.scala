package net.bdew.lib.multiblock

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

import scala.collection.mutable

object Tools {
  def canConnect(world: World, core: BlockPos, kind: ModuleType): Boolean = {
    world.getTileSafe[TileController](core) exists { t =>
      t.cfg.modules().get(kind).exists(_ > t.getNumOfModules(kind))
    }
  }

  def findConnections(world: World, start: BlockPos, kind: ModuleType): Iterable[BlockPos] =
    (start.neighbours.values flatMap { pos =>
      (world.getBlockEntity(pos) match {
        case t: TileModule => t.connected.value
        case t: TileController => Some(pos)
        case _ => None
      }) filter (x => canConnect(world, x, kind))
    }).toList.distinct

  def getConnectedNeighbours(w: World, core: BlockPos, pos: BlockPos, seen: mutable.Set[BlockPos]): Iterable[BlockPos] =
    pos.neighbours.values
      .filterNot(seen.contains)
      .flatMap(p => w.getTileSafe[TileModule](p))
      .filter(x => x.connected.contains(core))
      .map(_.getBlockPos)

  def findReachableModules(world: World, core: BlockPos): Set[BlockPos] = {
    val seen = mutable.Set.empty[BlockPos]
    val queue = mutable.Queue.empty[BlockPos]
    queue ++= getConnectedNeighbours(world, core, core, seen)
    while (queue.nonEmpty) {
      val current = queue.dequeue()
      seen.add(current)
      queue ++= getConnectedNeighbours(world, core, current, seen)
    }
    seen.toSet
  }
}
