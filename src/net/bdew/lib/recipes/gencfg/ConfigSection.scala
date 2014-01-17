/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.gencfg

abstract class CfgEntry

case class EntryDouble(v: Double) extends CfgEntry

case class EntryStr(v: String) extends CfgEntry

case class ConfigSection(pfx: String = "") extends CfgEntry with Iterable[(String, CfgEntry)] {
  var raw = Map.empty[String, CfgEntry].withDefault(x => sys.error("Config value '%s%s' is missing".format(pfx, x)))

  def iterator: Iterator[(String, CfgEntry)] = raw.iterator
  def filterType[T <: CfgEntry](cls: Class[T]): Iterable[(String, T)] = raw.filter(x => cls.isInstance(x._2)).map(x => x.asInstanceOf[(String, T)])

  def rawget[T](id: String, t: Class[T]): T = {
    val v = raw(id)
    if (t.isInstance(v))
      return v.asInstanceOf[T]
    else
      sys.error("Config value '%s%s' is of the wrong type, expected %s, got %s".format(pfx, id, t.getName, v.getClass.getName))
  }

  def set(id: String, v: CfgEntry) = raw += id -> v

  def getDouble(id: String) = rawget(id, classOf[EntryDouble]).v
  def getString(id: String) = rawget(id, classOf[EntryStr]).v
  def getSection(id: String) = rawget(id, classOf[ConfigSection])

  final val trueVals = Set("y", "true", "yes", "on")
  def getBoolean(id: String) = trueVals.contains(getString(id).toLowerCase)

  def hasValue(id: String) = raw.isDefinedAt(id)

  def getOrAddSection(id: String) = {
    if (raw.isDefinedAt(id)) {
      rawget(id, classOf[ConfigSection])
    } else {
      val newSection = new ConfigSection(pfx + id + ".")
      set(id, newSection)
      newSection
    }
  }

  def getInt(id: String) = getDouble(id).round.toInt
  def getFloat(id: String) = getDouble(id).toFloat
}


