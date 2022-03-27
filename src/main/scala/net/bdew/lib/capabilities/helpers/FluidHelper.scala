package net.bdew.lib.capabilities.helpers

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.capabilities.Capabilities.{CAP_FLUID_HANDLER, CAP_FLUID_HANDLER_ITEM}
import net.bdew.lib.capabilities.{CapAdapters, Capabilities}
import net.bdew.lib.items.ItemUtils
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidHandlerItem}

object FluidHelper {
  def hasFluidHandler(world: Level, pos: BlockPos, side: Direction): Boolean =
    getFluidHandler(world, pos, side).isDefined

  def hasFluidHandler(stack: ItemStack): Boolean =
    getFluidHandler(stack).isDefined

  def getFluidHandler(world: Level, pos: BlockPos, side: Direction): Option[IFluidHandler] = {
    val tile = world.getBlockEntity(pos)
    if (tile == null) return None
    val cap = tile.getCapability(CAP_FLUID_HANDLER, side)
    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      CapAdapters.get(CAP_FLUID_HANDLER).wrap(tile, side)
  }

  def getFluidHandler(stack: ItemStack): Option[IFluidHandlerItem] = {
    if (stack.isEmpty) return None
    val cap = stack.getCapability(CAP_FLUID_HANDLER_ITEM, null)

    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      CapAdapters.get(CAP_FLUID_HANDLER_ITEM).wrap(stack)
  }

  /**
   * Attempt to move fluid between 2 handlers
   *
   * @param from   source handler
   * @param to     destination handler
   * @param action if SIMULATE, drain will only be simulated.
   * @param max    max amount of fluid to move
   * @return stack of moved fluid or null if not possible
   */
  def pushFluid(from: IFluidHandler, to: IFluidHandler, action: FluidAction = FluidAction.EXECUTE, max: Int = Integer.MAX_VALUE): FluidStack = {
    val drainSim = from.drain(max, FluidAction.SIMULATE)
    if (!drainSim.isEmpty) {
      val fillSim = to.fill(drainSim.copy(), FluidAction.SIMULATE)
      if (fillSim > 0) {
        val drainSim2 = from.drain(fillSim, FluidAction.SIMULATE)
        if (!drainSim2.isEmpty) {
          val fillReal = to.fill(drainSim2.copy(), action)
          if (fillReal > 0) {
            if (action.execute)
              from.drain(fillReal, action)
            val tmp = drainSim2.copy()
            tmp.setAmount(fillReal)
            tmp
          } else FluidStack.EMPTY
        } else FluidStack.EMPTY
      } else FluidStack.EMPTY
    } else FluidStack.EMPTY
  }

  def blockFluidInteract(world: Level, pos: BlockPos, player: Player, hand: InteractionHand): Boolean = {
    val te = world.getBlockEntity(pos)
    val item = player.getItemInHand(hand)

    if (item.isEmpty || te == null) return false

    val cont = item.copy()
    cont.setCount(1)

    val resultStack = cont.getCapability(Capabilities.CAP_FLUID_HANDLER_ITEM).toScala.flatMap(itemCap =>
      te.getCapability(Capabilities.CAP_FLUID_HANDLER).toScala.flatMap(ourCap => {
        val fluid = itemCap.drain(Int.MaxValue, FluidAction.SIMULATE)
        if (fluid.isEmpty) {
          val moved = pushFluid(ourCap, itemCap)
          if (!moved.isEmpty) {
            world.playSound(null, pos, SoundEvents.BUCKET_FILL, player.getSoundSource, 1, 1)
            Some(itemCap.getContainer)
          } else None
        } else {
          val moved = pushFluid(itemCap, ourCap)
          if (!moved.isEmpty) {
            world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, player.getSoundSource, 1, 1)
            Some(itemCap.getContainer)
          } else None
        }
      })
    )

    resultStack.foreach(stack => {
      if (!player.isCreative) {
        if (item.getCount == 1)
          player.setItemInHand(hand, stack)
        else {
          item.shrink(1)
          if (!stack.isEmpty)
            ItemUtils.dropItemToPlayer(world, player, stack)
        }
      }
    })

    resultStack.nonEmpty
  }
}
