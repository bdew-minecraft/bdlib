/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.BdLib
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.data.DataSlotPosSet
import net.bdew.lib.multiblock.{MachineCore, ResourceProvider, Tools}
import net.minecraft.entity.player.EntityPlayer

trait TileController extends TileDataSlots {
  val modules = new DataSlotPosSet("modules", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE)

  def resources: ResourceProvider
  def cfg: MachineCore

  var acceptNewModules = true
  var revalidateOnNextTick = true
  var modulesChanged = true

  lazy val myPos = BlockRef(xCoord, yCoord, zCoord)

  def getNumOfModules(kind: String) = modules.flatMap(_.getTile[TileModule](getWorldObj)).count(_.kind == kind)

  def onModulesChanged()
  def onClick(player: EntityPlayer)

  def moduleConnected(module: TileModule): Boolean = {
    if (acceptNewModules) {
      modules.add(BlockRef(module.xCoord, module.yCoord, module.zCoord))
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      modulesChanged = true
      return true
    } else false
  }

  def moduleRemoved(module: TileModule) {
    modules.remove(BlockRef(module.xCoord, module.yCoord, module.zCoord))
    getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    revalidateOnNextTick = true
    modulesChanged = true
  }

  def onBreak() {
    acceptNewModules = false
    for (x <- modules.flatMap(_.getTile[TileModule](getWorldObj)))
      x.coreRemoved()
    modules.clear()
  }

  def validateModules() {
    modules.filter(x => {
      x.getTile[TileModule](getWorldObj).isEmpty || x.getBlock[BlockModule[TileModule]](getWorldObj).isEmpty
    }).foreach(x => {
      BdLib.logWarn("Block at %s is not a valid module, removing from machine %s at %d,%d,%d", x, this.getClass.getSimpleName, xCoord, yCoord, zCoord)
      modules.remove(x)
    })
    val reachable = Tools.findReachableModules(getWorldObj, myPos)
    val toRemove = modules.filterNot(reachable.contains).flatMap(_.getTile[TileModule](getWorldObj))
    acceptNewModules = false
    toRemove.foreach(moduleRemoved)
    toRemove.foreach(_.coreRemoved())
    acceptNewModules = true
    modulesChanged = true
    modules.map(x =>
      (x.getBlock[BlockModule[TileModule]](getWorldObj).orNull, x.getTile[TileModule](getWorldObj).orNull)
    ).filter({ case (a, b) => a.kind != b.kind }).foreach({ case (a, b) => sys.error("Type mismatch between Block/Tile Block=%s(%s) Tile=%s(%s)".format(a.kind, a, b.kind, b)) })
  }

  serverTick.listen(() => {
    if (revalidateOnNextTick) {
      revalidateOnNextTick = false
      validateModules()
    }
    if (modulesChanged) {
      modulesChanged = false
      onModulesChanged()
      lastChange = getWorldObj.getTotalWorldTime
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
  })
}
