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
case class ListElement[R](definition: Type[R], fromList: (NBTTagList, Int) => R)

object ListElement {
  def apply[R: ListElement] = implicitly[ListElement[R]]

  import Type._

  implicit val stringInList = ListElement(TString, _.getStringTagAt(_))
  implicit val compoundInList = ListElement(TCompound, _.getCompoundTagAt(_))
  implicit val intArrayInList = ListElement(TIntArray, _.func_150306_c(_))
  implicit val doubleInList = ListElement(TDouble, _.func_150309_d(_))
  implicit val floatInList = ListElement(TFloat, _.func_150308_e(_))
}