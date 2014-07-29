/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.BdLib
import net.bdew.lib.recipes.{RecipeLoader, Statement}

trait GenericConfigLoader extends RecipeLoader {
  val cfgStore: ConfigSection

  def processConfigStatement(section: ConfigSection, s: CfgStatement): Unit = s match {
    case CfgVal(id, v) =>
      BdLib.logInfo("Config: %s%s = %s", section.pfx, id, v)
      section.set(id, v)
    case CfgSub(id, st) => processConfigBlock(section.getOrAddSection(id), st)
    case x =>
      BdLib.logError("Can't process %s - this is a programing bug!", x)
  }

  def processConfigBlock(view: ConfigSection, block: List[CfgStatement]) = for (s <- block) processConfigStatement(view, s)

  override def processStatement(s: Statement) = s match {
    case StCfg(id, st) => processConfigBlock(cfgStore.getOrAddSection(id), st)
    case _ => super.processStatement(s)
  }
}

