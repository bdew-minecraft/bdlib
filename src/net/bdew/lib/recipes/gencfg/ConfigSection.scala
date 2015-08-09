/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.recipes.gencfg

import java.util.Locale

import net.bdew.lib.gui.Color

abstract class ConfigEntry

case class EntryDouble(v: Double) extends ConfigEntry

case class EntryStr(v: String) extends ConfigEntry

case class EntryNumList(v: List[Double]) extends ConfigEntry

case class ConfigSection(pfx: String = "") extends ConfigEntry with Iterable[(String, ConfigEntry)] {
  var raw = Map.empty[String, ConfigEntry].withDefault(x => sys.error("Config value '%s%s' is missing".format(pfx, x)))

  def iterator: Iterator[(String, ConfigEntry)] = raw.iterator
  def filterType[T <: ConfigEntry](cls: Class[T]): Iterable[(String, T)] = raw.filter(x => cls.isInstance(x._2)).map(x => x.asInstanceOf[(String, T)])

  def keys = raw.keys

  def getRaw[T](id: String, t: Class[T]): T = {
    val v = raw(id)
    if (t.isInstance(v))
      return v.asInstanceOf[T]
    else
      sys.error("Config value '%s%s' is of the wrong type, expected %s, got %s".format(pfx, id, t.getName, v.getClass.getName))
  }

  def set(id: String, v: ConfigEntry) = raw += id -> v

  def getDouble(id: String) = getRaw(id, classOf[EntryDouble]).v
  def getString(id: String) = getRaw(id, classOf[EntryStr]).v
  def getDoubleList(id: String) = getRaw(id, classOf[EntryNumList]).v
  def getSection(id: String) = getRaw(id, classOf[ConfigSection])

  final val trueVals = Set("y", "true", "yes", "on")
  def getBoolean(id: String) = trueVals.contains(getString(id).toLowerCase(Locale.US))

  def hasValue(id: String) = raw.isDefinedAt(id)

  def getOrAddSection(id: String) = {
    if (raw.isDefinedAt(id)) {
      getRaw(id, classOf[ConfigSection])
    } else {
      val newSection = new ConfigSection(pfx + id + ".")
      set(id, newSection)
      newSection
    }
  }

  def getInt(id: String) = getDouble(id).round.toInt
  def getFloat(id: String) = getDouble(id).toFloat
  def getIntList(id: String) = getDoubleList(id).map(_.round.toInt)
  def getFloatList(id: String) = getDoubleList(id).map(_.toFloat)

  def getColor(id: String) =
    raw(id) match {
      case EntryDouble(c) => Color.fromInt(c.toInt)
      case EntryNumList(r :: g :: b :: Nil) => Color(r.toFloat, g.toFloat, b.toFloat)
      case x => sys.error("Invalid color definition: %s".format(x))
    }
}


