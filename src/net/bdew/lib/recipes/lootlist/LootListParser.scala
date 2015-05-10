/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes.lootlist

import net.bdew.lib.recipes.gencfg.GenericConfigParser

/**
 * Parser mixin for loot lists
 * ident = DropsList(
 * {chance}% {stackRef}
 * ...
 * )
 */
trait LootListParser extends GenericConfigParser {
  def dropsEntry = (decimalNumber <~ "%") ~ spec ^^ { case n ~ s => (n.toDouble, s) }
  def ceDrops = "DropsList" ~> "(" ~> dropsEntry.* <~ ")" ^^ EntryLootList
  override def cfgValue = ceDrops | super.cfgValue
}

