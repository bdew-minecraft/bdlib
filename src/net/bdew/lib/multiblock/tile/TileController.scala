/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.BdLib
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{TileDataSlotsTicking, UpdateKind}
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.data.DataSlotPosSet
import net.bdew.lib.multiblock.{MachineCore, ResourceProvider, Tools}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos

import scala.reflect.ClassTag

trait TileController extends TileDataSlotsTicking {
  val modules = new DataSlotPosSet("modules", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE)

  def resources: ResourceProvider
  def cfg: MachineCore

  var acceptNewModules = true
  var revalidateOnNextTick = true
  var modulesChanged = true

  def getNumOfModules(kind: String) = getModuleTiles[TileModule].count(_.kind == kind)

  def getModuleTiles[T: ClassTag]: Set[T] = modules.flatMap(pos => getWorld.getTileSafe[T](pos)).toSet

  def getModuleBlocks[T: ClassTag]: Map[BlockPos, T] = modules.flatMap(pos => getWorld.getBlockSafe[T](pos) map (pos -> _)).toMap

  def getModulePositions(block: BlockModule[_]): Set[BlockPos] = modules.filter(pos => getWorld.getBlockState(pos).getBlock == block).toSet

  def onModulesChanged()
  def onClick(player: EntityPlayer)

  def moduleConnected(module: TileModule): Boolean = {
    if (acceptNewModules) {
      modules.add(module.getPos)
      sendUpdateToClients()
      modulesChanged = true
      return true
    } else false
  }

  def moduleRemoved(module: TileModule) {
    modules.remove(module.getPos)
    sendUpdateToClients()
    revalidateOnNextTick = true
    modulesChanged = true
  }

  def onBreak() {
    acceptNewModules = false
    getModuleTiles[TileModule].foreach(_.coreRemoved())
    modules.clear()
  }

  def validateModules() {
    modules.filter(x => {
      getWorld.getTileSafe[TileModule](x).isEmpty || getWorld.getBlockSafe[BlockModule[TileModule]](x).isEmpty
    }).foreach(x => {
      BdLib.logWarn("Block at %s is not a valid module, removing from machine %s at %s", x, this.getClass.getSimpleName, getPos)
      modules.remove(x)
    })
    val reachable = Tools.findReachableModules(getWorld, getPos)
    val toRemove = modules.filterNot(reachable.contains).flatMap(pos => getWorld.getTileSafe[TileModule](pos))
    acceptNewModules = false
    toRemove.foreach(moduleRemoved)
    toRemove.foreach(_.coreRemoved())
    acceptNewModules = true
    modulesChanged = true
    modules
      .map(mpos => (getWorld.getBlockSafe[BlockModule[TileModule]](mpos).orNull, getWorld.getTileSafe[TileModule](mpos).orNull))
      .filter({ case (a, b) => a.kind != b.kind })
      .foreach({ case (a, b) => sys.error("Type mismatch between Block/Tile Block=%s(%s) Tile=%s(%s)".format(a.kind, a, b.kind, b)) })
  }

  serverTick.listen(() => {
    if (revalidateOnNextTick) {
      revalidateOnNextTick = false
      validateModules()
    }
    if (modulesChanged) {
      modulesChanged = false
      onModulesChanged()
      lastChange = getWorld.getTotalWorldTime
      sendUpdateToClients()
    }
  })
}
