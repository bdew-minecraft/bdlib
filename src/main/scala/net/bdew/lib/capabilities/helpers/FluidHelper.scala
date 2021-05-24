package net.bdew.lib.capabilities.helpers

import net.bdew.lib.capabilities.CapAdapters
import net.bdew.lib.capabilities.Capabilities.{CAP_FLUID_HANDLER, CAP_FLUID_HANDLER_ITEM}
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidHandlerItem}

object FluidHelper {
  def hasFluidHandler(world: World, pos: BlockPos, side: Direction): Boolean =
    getFluidHandler(world, pos, side).isDefined

  def hasFluidHandler(stack: ItemStack): Boolean =
    getFluidHandler(stack).isDefined

  def getFluidHandler(world: World, pos: BlockPos, side: Direction): Option[IFluidHandler] = {
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
}
