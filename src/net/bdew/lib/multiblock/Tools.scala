/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

import scala.collection.mutable

object Tools {

  def canConnect(world: World, core: BlockPos, kind: String): Boolean = {
    val t = world.getTileSafe[TileController](core).getOrElse(return false)
    t.getNumOfModules(kind) < t.cfg.modules.getOrElse(kind, return false)
  }

  def findConnections(world: World, start: BlockPos, kind: String) =
    (start.neighbours.values flatMap { pos =>
      (world.getTileEntity(pos) match {
        case t: TileModule => t.connected.value
        case t: TileController => Some(pos)
        case _ => None
      }) filter (x => canConnect(world, x, kind))
    }).toList.distinct

  def getConnectedNeighbours(w: World, core: BlockPos, pos: BlockPos, seen: mutable.Set[BlockPos]) =
    pos.neighbours.values
      .filterNot(seen.contains)
      .flatMap(p => w.getTileSafe[TileModule](p))
      .filter(x => x.connected.contains(core))
      .map(_.getPos)

  def findReachableModules(world: World, core: BlockPos): Set[BlockPos] = {
    val seen = mutable.Set.empty[BlockPos]
    val queue = mutable.Queue.empty[BlockPos]
    queue ++= getConnectedNeighbours(world, core, core, seen)
    while (queue.nonEmpty) {
      val current = queue.dequeue()
      seen.add(current)
      queue ++= getConnectedNeighbours(world, core, current, seen)
    }
    return seen.toSet
  }
}
