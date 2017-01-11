/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

import net.bdew.lib.multiblock.tile.{TileController, TileModule}

import scala.reflect.ClassTag

/**
  * Special TileCommandHandler subclass for multiblock machines with a module that handles computer connections
  *
  * @tparam T TileEntity of the controller
  * @tparam M TileEntity of the module
  */
class ModuleCommandHandler[T <: TileController : ClassTag, M <: TileModule] extends TileCommandHandler[M] {
  /**
    * @param ctx call context
    * @return the controller TE
    */
  def getCore(ctx: CallContext[M]): T = {
    ctx.tile.getCoreAs[T].getOrElse(err("Not connected to valid machine"))
  }
}

case class ModuleSelector[M <: TileModule, C <: TileController](kind: String, cls: Class[C], commands: ModuleCommandHandler[C, M]) {
  def matches(te: M) = te.getCore.exists(cls.isInstance)
}