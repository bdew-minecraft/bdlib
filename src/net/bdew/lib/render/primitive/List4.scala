/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.primitive

/**
  * A list with 4 elements of type T
  */
case class List4[+T](v1: T, v2: T, v3: T, v4: T) {
  /**
    * Values as normal collection, lazy so it will be generated only if needed
    */
  lazy val vector = Vector(v1, v2, v3, v4)

  def combine[A, R](that: List4[A])(f: (T, A) => R): List4[R] =
    List4(
      f(v1, that.v1),
      f(v2, that.v2),
      f(v3, that.v3),
      f(v4, that.v4)
    )

  /**
    * Converts values to a new list using function f
    */
  def map[X](f: (T) => X): List4[X] = new List4(f(v1), f(v2), f(v3), f(v4))

  /**
    * Converts values to a new list using function f
    */
  def foreach(f: (T) => Unit) = {
    f(v1)
    f(v2)
    f(v3)
    f(v4)
  }

  /**
    * Fill an array (that must be at least offset+4 long) with the result of calling f on elements
    * This is faster than returning a new array since that would require a ClassTag and runtime reflection
    */
  def mapIntoArray[X](into: Array[X], f: (T) => X, offset: Int = 0) = {
    into(0 + offset) = f(v1)
    into(1 + offset) = f(v2)
    into(2 + offset) = f(v3)
    into(3 + offset) = f(v4)
  }
}

object List4 {
  def apply[T](x: IndexedSeq[T]): List4[T] = {
    if (x.length == 4)
      from(x)
    else
      throw new RuntimeException("Wrong number of elements for List4")
  }

  def from[T](f: (Int) => T) = List4(f(0), f(1), f(2), f(3))
}

