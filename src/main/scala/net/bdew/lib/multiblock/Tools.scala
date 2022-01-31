package net.bdew.lib.multiblock

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

import scala.collection.mutable

object Tools {
  def canConnect(world: Level, core: BlockPos, kind: ModuleType): Boolean = {
    world.getTileSafe[TileController](core) exists { t =>
      t.cfg.modules().get(kind).exists(_ > t.getNumOfModules(kind))
    }
  }

  def findConnections(world: Level, start: BlockPos, kind: ModuleType): Iterable[BlockPos] =
    (start.neighbours.values flatMap { pos =>
      (world.getBlockEntity(pos) match {
        case t: TileModule => t.connected.value
        case t: TileController => Some(pos)
        case _ => None
      }) filter (x => canConnect(world, x, kind))
    }).toList.distinct

  def getConnectedNeighbours(w: Level, core: BlockPos, pos: BlockPos, seen: mutable.Set[BlockPos]): Iterable[BlockPos] =
    pos.neighbours.values
      .filterNot(seen.contains)
      .flatMap(p => w.getTileSafe[TileModule](p))
      .filter(x => x.connected.contains(core))
      .map(_.getBlockPos)

  def findReachableModules(world: Level, core: BlockPos): Set[BlockPos] = {
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
