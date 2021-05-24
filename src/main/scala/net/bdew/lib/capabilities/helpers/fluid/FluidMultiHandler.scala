/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

/**
 * Composes multiple fluid handlers into one.
 */
abstract class FluidMultiHandler() extends IFluidHandler {
  def handlers: List[IFluidHandler]
  def mapTanks: List[(IFluidHandler, Int)]

  override def getTanks: Int = handlers.map(_.getTanks).sum

  private def run[T](slot: Int, f: (IFluidHandler, Int) => T): Option[T] =
    mapTanks.lift(slot).map(x => f.tupled(x))

  override def getFluidInTank(tank: Int): FluidStack =
    run(tank, _.getFluidInTank(_)).getOrElse(FluidStack.EMPTY)

  override def getTankCapacity(tank: Int): Int =
    run(tank, _.getTankCapacity(_)).getOrElse(0)

  override def isFluidValid(tank: Int, stack: FluidStack): Boolean =
    run(tank, _.isFluidValid(_, stack)).getOrElse(false)

  override def fill(original: FluidStack, action: IFluidHandler.FluidAction): Int = {
    val resource = original.copy()
    val starting = resource.getAmount
    for (tank <- handlers) {
      val filled = tank.fill(resource.copy(), action)
      resource.shrink(filled)
      if (resource.isEmpty)
        return starting
    }
    starting - resource.getAmount
  }

  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = {
    for (tank <- handlers) {
      val drained = tank.drain(resource, action)
      if (!drained.isEmpty) return drained
    }
    FluidStack.EMPTY
  }

  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = {
    for (tank <- handlers) {
      val drained = tank.drain(maxDrain, action)
      if (!drained.isEmpty) return drained
    }
    FluidStack.EMPTY
  }
}

class FluidMultiHandlerStatic(val handlers: List[IFluidHandler]) extends FluidMultiHandler {
  override val mapTanks: List[(IFluidHandler, Int)] = handlers.flatMap(x => (0 until x.getTanks).map(t => (x, t)))
}

class FluidMultiHandlerDynamic(getHandlers: () => List[IFluidHandler]) extends FluidMultiHandler {
  override def handlers: List[IFluidHandler] = getHandlers()
  override def mapTanks: List[(IFluidHandler, Int)] = getHandlers().flatMap(x => (0 until x.getTanks).map(t => (x, t)))
}


object FluidMultiHandler {
  def wrap(handlers: List[IFluidHandler]): IFluidHandler = {
    if (handlers.length == 1)
      handlers.head
    else
      new FluidMultiHandlerStatic(handlers)
  }

  def wrap(handlers: IFluidHandler*): IFluidHandler = {
    if (handlers.length == 1)
      handlers.head
    else
      new FluidMultiHandlerStatic(handlers.toList)
  }

  def dynamic(getHandlers: () => List[IFluidHandler]): IFluidHandler = {
    new FluidMultiHandlerDynamic(getHandlers)
  }
}