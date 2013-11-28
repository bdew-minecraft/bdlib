/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.gencfg

import net.bdew.lib.recipes.Statement

abstract class CfgStatement

case class CfgVal(id: String, value: CfgEntry) extends CfgStatement

case class CfgSub(id: String, vals: List[CfgStatement]) extends CfgStatement

case class StCfg(id: String, vals: List[CfgStatement]) extends Statement




