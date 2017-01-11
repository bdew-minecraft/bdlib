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
import java.util.Locale

import com.google.common.base.Optional
import net.bdew.lib.Misc

import scala.collection.JavaConversions._

/**
  * Factory to create properties for scala enumerations
  */
object EnumerationProperty {
  def create[T <: Enumeration](enum: T, name: String) = {
    new SimpleProperty[enum.Value](name, classOf[enum.Value]) {
      override def getName(value: enum.Value): String = value.toString.toLowerCase(Locale.US)
      override def getAllowedValues: util.Collection[enum.Value] = enum.values.toList
      override def parseValue(value: String): Optional[enum.Value] =
        Misc.toOptional(enum.values.find(_.toString.toLowerCase(Locale.US) == value))
    }
  }

  def createUnlisted[T <: Enumeration](enum: T, name: String) = new SimpleUnlistedProperty(name, classOf[enum.Value])
}
