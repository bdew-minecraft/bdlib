/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.mixins

import net.bdew.lib.data.base.DataSlotVal

abstract class DataSlotOption[T] extends DataSlotVal[Option[T]] {
  override def default: Option[T] = None

  def set(v: T): Unit = {
    require(v != null, "Null should never be used with DataSlotOption")
    update(Some(v))
  }

  def unset(): Unit = {
    update(None)
  }

  override def isSame(v: Option[T]): Boolean = {
    require(v != null, "Null should never be used with DataSlotOption")
    super.isSame(v)
  }

  override def :=(that: Option[T]): Unit = {
    require(that != null, "Null should never be used with DataSlotOption")
    super.:=(that)
  }

  override def :!=(that: Option[T]): Boolean = {
    require(that != null, "Null should never be used with DataSlotOption")
    super.:!=(that)
  }

  override def :==(that: Option[T]): Boolean = {
    require(that != null, "Null should never be used with DataSlotOption")
    super.:==(that)
  }

  override def update(v: Option[T], notify: Boolean = true): Unit = {
    require(v != null, "Null should never be used with DataSlotOption")
    super.update(v, notify)
  }
}
