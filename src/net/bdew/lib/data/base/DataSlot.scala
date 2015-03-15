/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.minecraft.nbt.NBTTagCompound

/**
 * Base trait for all data slots
 */
trait DataSlot {
  /**
   * Tile Entity that owns this slot
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val parent: DataSlotContainer

  /**
   * Unique name
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val name: String

  parent.dataSlots += (name -> this)

  // Where should it sync
  var updateKind = Set(UpdateKind.GUI)

  def save(t: NBTTagCompound, kind: UpdateKind.Value)
  def load(t: NBTTagCompound, kind: UpdateKind.Value)

  def execWithChangeNotify[T](f: => T): T = {
    val v = f
    parent.dataSlotChanged(this)
    v
  }

  def execWithChangeNotifyConditional[T](f: => T, condition: T => Boolean): T = {
    val v = f
    if (condition(v))
      parent.dataSlotChanged(this)
    v
  }

  def setUpdate(vals: UpdateKind.Value*): this.type = {
    updateKind = vals.toSet
    return this
  }
}
