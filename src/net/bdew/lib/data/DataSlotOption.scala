/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.DataSlotVal

abstract class DataSlotOption[T] extends DataSlotVal[Option[T]] {
  override var cval: Option[T] = None

  def set(v: T) {
    if (v == null) sys.error("Null should never be used with DataSlotOption")
    cval = Some(v)
  }

  def unset() {
    cval = None
  }

  override def isSame(v: Option[T]) = {
    if (v == null) sys.error("Null should never be used with DataSlotOption")
    super.isSame(v)
  }
  override def :=(that: Option[T]) = {
    if (that == null) sys.error("Null should never be used with DataSlotOption")
    super.:=(that)
  }
  override def :!=(that: Option[T]) = {
    if (that == null) sys.error("Null should never be used with DataSlotOption")
    super.:!=(that)
  }
  override def :==(that: Option[T]) = {
    if (that == null) sys.error("Null should never be used with DataSlotOption")
    super.:==(that)
  }
  override def update(v: Option[T]) = {
    if (v == null) sys.error("Null should never be used with DataSlotOption")
    super.update(v)
  }
}
