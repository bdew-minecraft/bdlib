/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.property

import net.minecraft.block.state.IBlockState
import net.minecraftforge.common.property.{IExtendedBlockState, IUnlistedProperty}

class SimpleUnlistedProperty[T](val name: String, cls: Class[T]) extends IUnlistedProperty[T] {
  override def getType = cls
  override def getName = name
  override def valueToString(value: T) = value.toString
  override def isValid(value: T): Boolean = true

  def get(state: IBlockState): Option[T] = {
    if (state.isInstanceOf[IExtendedBlockState]) {
      val ebs = state.asInstanceOf[IExtendedBlockState]
      if (ebs.getUnlistedNames.contains(this)) {
        Option(ebs.getValue(this))
      } else None
    } else None
  }
}
