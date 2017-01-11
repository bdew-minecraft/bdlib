/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.helpers

import net.minecraft.util.NonNullList

import scala.collection.TraversableOnce

object NonNullHelper {
  def toNonNullList[T](input: TraversableOnce[T]): NonNullList[T] = {
    val list = NonNullList.create[T]()
    input.foreach(x => if (x != null) list.add(x))
    list
  }
}
