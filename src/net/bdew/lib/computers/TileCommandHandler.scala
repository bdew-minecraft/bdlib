/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

import net.minecraft.tileentity.TileEntity

/**
  * Describes a set of commands for a type of tile entity T
  */
class TileCommandHandler[T <: TileEntity] {

  /**
    * A single command handler
    *
    * @param apply    handler
    * @param isDirect if true - can be executed anywhere, otherwise MUST be executed in server thread
    */
  case class Handler(apply: CallContext[T] => Result, isDirect: Boolean)

  /**
    * List of command handlers
    */
  var commands = Map.empty[String, Handler]

  /**
    * Define a command
    *
    * @param name   method name (as seen by the computer)
    * @param direct if true - can be executed anywhere, otherwise MUST be executed in server thread
    * @param f      body of the command
    */
  def command(name: String, direct: Boolean = false)(f: CallContext[T] => Result) = commands += (name -> Handler(f, direct))

  /**
    * Throw an error that will be returned to the computer
    */
  def err(err: String) = throw new ComputerException(err)

  lazy val commandNames = commands.keys.toArray
  lazy val idToCommand = (commandNames.zipWithIndex map (_.swap)).toMap
}
