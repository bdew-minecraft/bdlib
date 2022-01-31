package net.bdew.lib.block

import net.bdew.lib.BdLib
import net.bdew.lib.managers.TEManager
import net.bdew.lib.tile.{TileTickingClient, TileTickingServer}
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.{Block, EntityBlock}
import net.minecraft.world.level.{BlockGetter, Level}

/**
 * Mixin for blocks that have a TileEntity
 *
 * @tparam T type of TE
 */
trait HasTE[T <: BlockEntity] extends Block with EntityBlock {
  lazy val teType: BlockEntityType[T] = TEManager.blockMap(this).asInstanceOf[BlockEntityType[T]]

  override def newBlockEntity(pos: BlockPos, state: BlockState): T = teType.create(pos, state)

  /**
   * Get TE for a block
   */
  def getTE(w: Level, pos: BlockPos): T = {
    var t = teType.getBlockEntity(w, pos)
    if (t == null) {
      if (w.getBlockState(pos).getBlock == this) {
        BdLib.logWarn("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, pos.getX, pos.getY, pos.getZ)
        t = newBlockEntity(pos, w.getBlockState(pos))
        w.setBlockEntity(t)
      } else {
        sys.error(s"Attempt to get TE ${teType.getRegistryName} on a mismatched block ${w.getBlockState(pos)} at $pos")
      }
    }
    t
  }

  /**
   * Get TE for a block, if available. If this is in client it might not be available yet.
   */
  def getTE(w: BlockGetter, pos: BlockPos): Option[T] = {
    try {
      w match {
        case ww: Level =>
          Some(getTE(ww, pos))
        case _ =>
          Option(teType.getBlockEntity(w, pos))
      }
    } catch {
      case e: Throwable =>
        BdLib.logWarnException("Error retrieving TE of type %s at %s (world is %s)", e, getClass.getName, pos.toString, w.getClass.getName)
        None
    }
  }
}

trait HasTETickingServer[T <: BlockEntity with TileTickingServer] extends HasTE[T] {
  private lazy val serverTicker = new BlockEntityTicker[T] {
    override def tick(world: Level, pos: BlockPos, state: BlockState, entity: T): Unit = entity.serverTick.trigger()
  }

  override def getTicker[E <: BlockEntity](world: Level, state: BlockState, et: BlockEntityType[E]): BlockEntityTicker[E] =
    if (world.isClientSide) null else serverTicker.asInstanceOf[BlockEntityTicker[E]]
}

trait HasTETickingClient[T <: BlockEntity with TileTickingClient] extends HasTE[T] {
  private lazy val clientTicker = new BlockEntityTicker[T] {
    override def tick(world: Level, pos: BlockPos, state: BlockState, entity: T): Unit = entity.clientTick.trigger()
  }

  override def getTicker[E <: BlockEntity](world: Level, state: BlockState, et: BlockEntityType[E]): BlockEntityTicker[E] =
    if (world.isClientSide) clientTicker.asInstanceOf[BlockEntityTicker[E]] else null
}

trait HasTETickingBoth[T <: BlockEntity with TileTickingClient with TileTickingServer] extends HasTE[T] {
  private lazy val clientTicker = new BlockEntityTicker[T] {
    override def tick(world: Level, pos: BlockPos, state: BlockState, entity: T): Unit = entity.clientTick.trigger()
  }

  private lazy val serverTicker = new BlockEntityTicker[T] {
    override def tick(world: Level, pos: BlockPos, state: BlockState, entity: T): Unit = entity.clientTick.trigger()
  }

  override def getTicker[E <: BlockEntity](world: Level, state: BlockState, et: BlockEntityType[E]): BlockEntityTicker[E] =
    if (world.isClientSide) clientTicker.asInstanceOf[BlockEntityTicker[E]] else serverTicker.asInstanceOf[BlockEntityTicker[E]]
}