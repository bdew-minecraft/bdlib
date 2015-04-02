/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.minecraft.nbt.NBTTagCompound

import scala.language.implicitConversions

package object nbt {
  implicit def compound2rich(p: NBTTagCompound): RichNBTTagCompound = new RichNBTTagCompound(p)
}
