/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.machine

import net.bdew.lib.recipes.gencfg.ConfigSection

trait PoweredMachine {
  def tuning: ConfigSection
  lazy val maxReceivedEnergy = tuning.getFloat("MaxReceivedEnergy")
  lazy val activationEnergy = tuning.getFloat("ActivationEnergy")
  lazy val maxStoredEnergy = tuning.getFloat("MaxStoredEnergy")
}
