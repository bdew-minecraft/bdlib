/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}

object NullCapabilityProvider extends ICapabilityProvider {
  override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = null.asInstanceOf[T]
  override def hasCapability(capability: Capability[_], facing: EnumFacing): Boolean = false
}
