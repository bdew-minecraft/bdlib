/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base._
import net.bdew.lib.data.mixins.{DataSlotNBT, DataSlotNBTOption, DataSlotNumeric}
import net.bdew.lib.nbt.Type

abstract class DataSlotSimpleNBT[T: Type] extends DataSlotNBT[T] {
  override def nbtType = Type[T]
}

abstract class DataSlotNumericNBT[T: Numeric : Type] extends DataSlotSimpleNBT[T] with DataSlotNumeric[T] {
  override def numeric = implicitly[Numeric[T]]
}

case class DataSlotBoolean(name: String, parent: DataSlotContainer, default: Boolean = false) extends DataSlotSimpleNBT[Boolean]

case class DataSlotString(name: String, parent: DataSlotContainer, default: String = "") extends DataSlotSimpleNBT[String]

case class DataSlotByte(name: String, parent: DataSlotContainer, default: Byte = 0) extends DataSlotNumericNBT[Byte]

case class DataSlotShort(name: String, parent: DataSlotContainer, default: Short = 0) extends DataSlotNumericNBT[Short]

case class DataSlotInt(name: String, parent: DataSlotContainer, default: Int = 0) extends DataSlotNumericNBT[Int]

case class DataSlotLong(name: String, parent: DataSlotContainer, default: Long = 0) extends DataSlotNumericNBT[Long]

case class DataSlotFloat(name: String, parent: DataSlotContainer, default: Float = 0) extends DataSlotNumericNBT[Float]

case class DataSlotDouble(name: String, parent: DataSlotContainer, default: Double = 0) extends DataSlotNumericNBT[Double]

case class DataSlotOption[T: Type](name: String, parent: DataSlotContainer) extends DataSlotNBTOption[T] {
  override def nbtType = Type[T]
}
