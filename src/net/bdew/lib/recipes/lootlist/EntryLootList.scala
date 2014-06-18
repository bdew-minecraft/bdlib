/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.lootlist

import net.bdew.lib.recipes.StackRef
import net.bdew.lib.recipes.gencfg.CfgEntry

/**
 * Allos definition of random-weighted loot/drop lists in config sections
 * @param list List on (chance percent, stack reference) pairs, resolve to
 *             real ItemStacks with LootListLoader.resolveLootList
 */
case class EntryLootList(list: List[(Int, StackRef)]) extends CfgEntry




