package net.bdew.lib.multiblock.tile

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.multiblock.data.DataSlotPos
import net.bdew.lib.multiblock.{ModuleType, Tools}
import net.minecraft.util.math.BlockPos

import scala.reflect.ClassTag

trait TileModule extends TileDataSlots {
  val kind: ModuleType
  val connected: DataSlotPos = DataSlotPos("connected", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER, UpdateKind.MODEL_DATA)

  def getCoreAs[T <: TileController : ClassTag]: Option[T] = connected flatMap (p => getLevel.getTileSafe[T](p))

  def getCore: Option[TileController] = getCoreAs[TileController]

  def connect(target: TileController): Unit = {
    if (target.moduleConnected(this)) {
      connected.set(target.getBlockPos)
      getLevel.updateNeighborsAt(getBlockPos, getBlockState.getBlock)
      sendUpdateToClients()
    }
  }

  def coreRemoved(): Unit = {
    connected.unset()
    getLevel.updateNeighborsAt(getBlockPos, getBlockState.getBlock)
    sendUpdateToClients()
  }

  def onBreak(): Unit = {
    getCore foreach (_.moduleRemoved(this))
  }

  def canConnectToCore(br: BlockPos) = true

  def tryConnect(): Unit = {
    if (getCore.isEmpty) {
      for {
        conn <- Tools.findConnections(getLevel, getBlockPos, kind).headOption if canConnectToCore(conn)
        core <- getLevel.getTileSafe[TileController](conn)
      } {
        connect(core)
      }
    }
  }
}
