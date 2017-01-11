/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.property

import java.util

import net.minecraft.block.properties.IProperty

abstract class SimpleProperty[T <: Comparable[T]](val name: String, cls: Class[T]) extends IProperty[T] {
  override def getValueClass: Class[T] = cls
  override def getName: String = name
  override def getName(value: T): String
  override def getAllowedValues: util.Collection[T]
}
