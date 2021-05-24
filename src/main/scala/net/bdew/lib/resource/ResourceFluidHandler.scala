package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

class ResourceFluidHandler(slot: DataSlotResource) extends IFluidHandler {
  override def getTanks: Int = 1
  override def getTankCapacity(tank: Int): Int = slot.capacity

  override def getFluidInTank(tank: Int): FluidStack =
    slot.resource flatMap { res =>
      res.kind match {
        case FluidResource(f) => Some(new FluidStack(f, res.amount.toInt))
        case _ => None
      }
    } getOrElse FluidStack.EMPTY

  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = slot.canAccept(Resource.from(stack))

  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
    if (isFluidValid(0, resource))
      slot.fill(Resource.from(resource), onlyRound = true, doFill = action.execute()).toInt
    else
      0

  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = {
    (for {
      res <- slot.drain(resource.getAmount, true, false)
      rKind <- Misc.asInstanceOpt(res.kind, classOf[FluidResource]) if rKind.fluid.isSame(resource.getFluid)
    } yield {
      if (action.execute())
        slot.drain(res.amount, true, true)
      new FluidStack(rKind.fluid, res.amount.toInt)
    }) getOrElse FluidStack.EMPTY
  }

  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = {
    (for {
      res <- slot.drain(maxDrain, true, false)
      rKind <- Misc.asInstanceOpt(res.kind, classOf[FluidResource])
    } yield {
      if (action.execute())
        slot.drain(res.amount, true, true)
      new FluidStack(rKind.fluid, res.amount.toInt)
    }) getOrElse FluidStack.EMPTY
  }
}
