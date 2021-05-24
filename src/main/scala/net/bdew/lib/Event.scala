package net.bdew.lib

class EventBase[T] extends {
  var listeners = Seq.empty[T]
  def listen(x: T): Unit = listeners :+= x
  def hasListeners: Boolean = listeners.nonEmpty
}

class Event0 extends EventBase[() => Unit] {
  def trigger(): Unit = for (x <- listeners) x()
}

class Event1[T] extends EventBase[T => Unit] {
  def trigger(p1: T): Unit = for (x <- listeners) x(p1)
}

class Event2[T, R] extends EventBase[(T, R) => Unit] {
  def trigger(p1: T, p2: R): Unit = for (x <- listeners) x(p1, p2)
}

object Event {
  def apply() = new Event0
  def apply[T] = new Event1[T]
  def apply[T, R] = new Event2[T, R]
}