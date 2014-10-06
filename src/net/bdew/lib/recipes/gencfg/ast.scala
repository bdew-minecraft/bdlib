/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.recipes.ConfigStatement

abstract class CfgEntry

case class CfgVal(id: String, value: ConfigEntry) extends CfgEntry

case class CfgSection(id: String, vals: List[CfgEntry]) extends CfgEntry

case class CsCfgSection(id: String, vals: List[CfgEntry]) extends ConfigStatement




