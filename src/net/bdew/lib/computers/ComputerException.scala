/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

/**
  * Special exception type that will be passed to the computer as an error (if supported by the mod)
  */
class ComputerException(msg: String) extends RuntimeException(msg)
