/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.multiblock.network.MsgOutputCfg

case class MsgOutputCfgRSMode(output: Int, rsMode: RSMode.Value) extends MsgOutputCfg

trait OutputConfigRSControllable extends OutputConfig {
  var rsMode: RSMode.Value
}