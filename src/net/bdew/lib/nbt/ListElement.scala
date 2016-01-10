/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt.NBTTagList

import scala.annotation.implicitNotFound

@implicitNotFound("Type ${R} can't currently be retrieved from NBT lists")
case class ListElement[R](typeId: Int, definition: Type[R], fromList: (NBTTagList, Int) => R)

object ListElement {
  def apply[R: ListElement] = implicitly[ListElement[R]]

  import Type._

  private def mkListElement[R](td: TypeDef[_, R], fromList: (NBTTagList, Int) => R) = ListElement(td.id, td, fromList)

  implicit val stringInList = mkListElement(TString, _.getStringTagAt(_))
  implicit val compoundInList = mkListElement(TCompound, _.getCompoundTagAt(_))
  implicit val intArrayInList = mkListElement(TIntArray, _.getIntArrayAt(_))
  implicit val doubleInList = mkListElement(TDouble, _.getDoubleAt(_))
  implicit val floatInList = mkListElement(TFloat, _.getFloatAt(_))
}