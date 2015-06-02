/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

class EventBase[T] extends {
  var listeners = Seq.empty[T]
  def listen(x: T) = listeners :+= x
  def hasListeners = listeners.nonEmpty
}

class Event0 extends EventBase[() => Unit] {
  def trigger() = for (x <- listeners) x()
}

class Event1[T] extends EventBase[(T) => Unit] {
  def trigger(p1: T) = for (x <- listeners) x(p1)
}

class Event2[T, R] extends EventBase[(T, R) => Unit] {
  def trigger(p1: T, p2: R) = for (x <- listeners) x(p1, p2)
}

object Event {
  def apply() = new Event0
  def apply[T] = new Event1[T]
  def apply[T, R] = new Event2[T, R]
}