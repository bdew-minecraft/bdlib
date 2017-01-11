/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import java.util.concurrent.Callable

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

import scala.reflect.ClassTag

/**
  * Null implementation of capability storage, for use when providing an implementation makes no sense
  */
class NoStorage[T] extends Capability.IStorage[T] {
  override def writeNBT(capability: Capability[T], instance: T, side: EnumFacing): NBTBase =
    throw new UnsupportedOperationException("Default capability storage for %s should not be used".format(capability.getName))

  override def readNBT(capability: Capability[T], instance: T, side: EnumFacing, nbt: NBTBase): Unit =
    throw new UnsupportedOperationException("Default capability storage for %s should not be used".format(capability.getName))
}

/**
  * Null implementation of capability factory, for use when providing an implementation makes no sense
  */
class NoFactory[T: ClassTag] extends Callable[T] {
  override def call(): T =
    throw new UnsupportedOperationException("Default capability factory for %s should not be used".format(implicitly[ClassTag[T]].runtimeClass.getName))
}