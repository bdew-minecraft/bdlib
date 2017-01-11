/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

import net.bdew.lib.computers.Result._

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
  * Provides mod-specific encoding of result values
  */
trait ResultConverter {
  def encode(v: Result): AnyRef
  def wrap(v: Result): Array[AnyRef]
}

/**
  * Implementation for mods that use simple java boxed values and collections, handles everything except futures
  */
abstract class SimpleResultConverter extends ResultConverter {

  def handleFuture(future: Future[Result]): AnyRef

  override def encode(r: Result): AnyRef = r match {
    case ResInt(v) => Int.box(v)
    case ResFloat(v) => Float.box(v)
    case ResDouble(v) => Double.box(v)
    case ResBoolean(v) => Boolean.box(v)
    case ResString(v) => v
    case ResMap(v) => v.mapValues(encode).asJava
    case ResList(v) => v.zipWithIndex.map({ case (vv, kk) => kk + 1 -> encode(vv) }).toMap.asJava
    case ResFuture(v) => handleFuture(v)
    case Null => null
    case a: ResArray => wrap(a)

  }

  override def wrap(v: Result): Array[AnyRef] = v match {
    case ResArray(a) => a.map(encode)
    case Null => Array(null)
    case x => Array(encode(x))
  }
}