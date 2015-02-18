/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.BdLib
import net.bdew.lib.recipes.{ConfigStatement, RecipeLoader}

trait GenericConfigLoader extends RecipeLoader {
  val cfgStore: ConfigSection

  def processConfigStatement(section: ConfigSection, s: CfgEntry): Unit = s match {
    case CfgVal(id, v) =>
      BdLib.logDebug("Config: %s%s = %s", section.pfx, id, v)
      section.set(id, v)
    case CfgSection(id, st) => processConfigBlock(section.getOrAddSection(id), st)
    case x =>
      BdLib.logError("Can't process %s - this is a programing bug!", x)
  }

  def processConfigBlock(view: ConfigSection, block: List[CfgEntry]): Unit =
    for (s <- block) processConfigStatement(view, s)

  override def processConfigStatement(s: ConfigStatement) = s match {
    case CsCfgSection(id, st) => processConfigBlock(cfgStore.getOrAddSection(id), st)
    case _ => super.processConfigStatement(s)
  }
}

