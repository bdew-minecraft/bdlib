/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.async

import scala.concurrent.{Future, Promise}

object Async {
  def inOtherThread[T](f: => T) = {
    val p = Promise[T]()
    val t = new Thread(new Runnable {
      override def run(): Unit =
        try {
          p.success(f)
        } catch {
          case t: Throwable => p.failure(t)
        }
    })
    t.start()
    p.future
  }

  def inServerThread[T](f: => T) = Future(f)(ServerTickExecutionContext)
}
