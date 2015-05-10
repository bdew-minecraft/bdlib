/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes.lootlist

import net.bdew.lib.BdLib
import net.bdew.lib.recipes.StackRef
import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 * Loader mixin for loot lists
 */
trait LootListLoader extends GenericConfigLoader {
  def resolveLootList(entry: EntryLootList): List[(Double, ItemStack)] = resolveLootList(entry.list)
  def resolveLootList(list: List[(Double, StackRef)]) =
    (for ((chance, ref) <- list) yield {
      try {
        val itemStack = getConcreteStack(ref)
        if (itemStack == null) {
          BdLib.logWarn("Unable to resolve %s: null returned", ref)
          None
        } else {
          if (itemStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
            BdLib.logDebug("meta/damage is unset in %s, defaulting to 0", ref)
            itemStack.setItemDamage(0)
          }
          Some((chance, itemStack))
        }
      } catch {
        case e: Throwable =>
          BdLib.logWarn("Unable to resolve %s: %s", ref, e.getMessage)
          None
      }
    }).flatten
}
