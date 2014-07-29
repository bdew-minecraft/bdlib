/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes

import java.lang.reflect.Modifier

import net.minecraft.block.Block
import net.minecraft.item.Item

import scala.reflect.ClassTag

object Vanilla {

  // Maps of vanilla blocks and items (without tile. / item. prefixes)

  lazy val blocks = mkMap[Block](_.getUnlocalizedName.stripPrefix("tile.")).withDefault(x => throw new StatementError("Block %s not found".format(x)))
  lazy val items = mkMap[Item](_.getUnlocalizedName.stripPrefix("item.")).withDefault(x => throw new StatementError("Item %s not found".format(x)))

  /**
   * Generates the maps
   * @tparam T The base class
   * @param namer A function that converts objects to names
   */
  private def mkMap[T](namer: T => String)(implicit t: ClassTag[T]) = {
    val c = t.runtimeClass
    // Grab all the fields
    c.getFields.filter(x => {
      // Keep only static fields that match the base Class
      c.isAssignableFrom(x.getType) && Modifier.isStatic(x.getModifiers)
    }).map(x => {
      // Retrieve the static value
      val f = x.get(null).asInstanceOf[T]
      // Convert to name -> object tuples
      namer(f) -> f
    }).toMap // and finally to a map
  }
}
