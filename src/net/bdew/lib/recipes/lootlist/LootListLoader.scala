/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.lootlist

import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.minecraftforge.oredict.OreDictionary
import net.bdew.lib.recipes.StackRef
import net.minecraft.item.ItemStack

/**
 * Loader mixin for loot lists
 */
trait LootListLoader extends GenericConfigLoader {
  def resolveLootList(entry: EntryLootList): List[(Int, ItemStack)] = resolveLootList(entry.list)
  def resolveLootList(list: List[(Int, StackRef)]) =
    (for ((chance, ref) <- list) yield {
      try {
        val itemStack = getConcreteStack(ref)
        if (itemStack == null) {
          log.warning("Unable to resolve %s: null returned".format(ref))
          None
        } else {
          if (itemStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
            log.info("meta/damage is unset in %s, defaulting to 0".format(ref))
            itemStack.setItemDamage(0)
          }
          Some((chance, itemStack))
        }
      } catch {
        case e: Throwable =>
          log.warning("Unable to resolve %s: %s".format(ref, e.getMessage))
          None
      }
    }).flatten
}
