package net.bdew.lib.multiblock.tile

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla.pimpModelData
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.interact.{CIOutputFaces, MIOutput}
import net.bdew.lib.multiblock.render.OutputFaceProperty
import net.bdew.lib.tile.{TileExtended, TileTickingServer}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.ModelData

abstract class TileOutput[T <: OutputConfig](teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state)
  with TileModule with MIOutput[T] with TileTickingServer {

  override def getCore: Option[CIOutputFaces] = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: Direction): T

  var rescanFaces = false

  def getCfg(dir: Direction): Option[T] =
    for {
      core <- getCore
      oNum <- core.outputFaces.get(BlockFace(getBlockPos, dir))
      cfgGen <- core.outputConfig.get(oNum)
      cfg <- Misc.asInstanceOpt(cfgGen, outputConfigType)
    } yield cfg

  serverTick.listen(() => {
    if (rescanFaces) {
      rescanFaces = false
      doRescanFaces()
    }
  })

  override def tryConnect(): Unit = {
    super.tryConnect()
    if (connected.isDefined) rescanFaces = true
  }

  def canConnectToFace(d: Direction): Boolean

  def onConnectionsChanged(added: Set[Direction], removed: Set[Direction]): Unit = {}

  def doRescanFaces(): Unit = {
    getCore foreach { core =>
      val connections = (
        Direction.values()
          filterNot { dir => core.modules.contains(getBlockPos.offset(dir.getNormal)) }
          filter canConnectToFace
        ).toSet
      val known = core.outputFaces.filter(_._1.pos == getBlockPos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(getBlockPos, x))
      toAdd.foreach(x => core.newOutput(getBlockPos, x, makeCfgObject(x)))
      if (toAdd.nonEmpty || toRemove.nonEmpty) {
        onConnectionsChanged(toAdd, toRemove)
        sendUpdateToClients()
      }
    }
  }

  override def getModelData: ModelData = {
    val faces = for {
      core <- getCore.toList
      face <- Direction.values()
      output <- core.outputFaces.get(BlockFace(getBlockPos, face))
    } yield face -> output
    super.getModelData.withData(OutputFaceProperty, faces.toMap)
  }
}
