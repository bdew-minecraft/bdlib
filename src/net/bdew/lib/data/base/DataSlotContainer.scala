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
import net.minecraft.world.World

import scala.collection.mutable

/**
 * Base Trait for objects that can contain Data Slots, this allows implementing them on things that aren't TEs
 */
trait DataSlotContainer {

  // Can't use getWorldObj - it fails to get remapped and stuff breaks in obfuscated environment
  def getWorldObject: World

  /**
   * Called when a dataslot value changes
   */
  def dataSlotChanged(slot: DataSlot): Unit

  /**
   * Register a function to run on every server tick
   */
  def onServerTick(f: () => Unit): Unit

  /**
   * List of dataslots in this container
   */
  val dataSlots = mutable.HashMap.empty[String, DataSlot]

  /**
   * The value of getTotalWorldTime when the last change happened
   */
  var lastChange = 0L

  /**
   * The value of getTotalWorldTime when the last GUI packet was sent happened
   */
  var lastGuiPacket = 0L

  final val TRACE = false

  def doSave(kind: UpdateKind.Value, t: NBTTagCompound) {
    if (kind == UpdateKind.GUI)
      t.setLong("BDLib_TS", getWorldObject.getTotalWorldTime)
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      s.save(t, kind)
      if (TRACE) printf("%s: %s S=> %s\n".format(kind, n, t.getTag(n)))
    }
  }

  def doLoad(kind: UpdateKind.Value, t: NBTTagCompound) {
    if (kind == UpdateKind.GUI) {
      val ts = t.getLong("BDLib_TS")
      if (ts > lastGuiPacket) {
        lastGuiPacket = ts
      } else {
        return
      }
    }
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      if (TRACE) printf("%s: %s L<= %s\n".format(kind, n, t.getTag(n)))
      s.load(t, kind)
    }
  }
}


