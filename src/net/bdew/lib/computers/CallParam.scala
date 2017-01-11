/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.computers

sealed trait CallParam[R] {
  def name: String
  def unapply(v: Option[Any]): Option[R]
  def ? = POption(this)
}

case class ParamSimple[R, T](name: String, runtimeClass: Class[R]) extends CallParam[R] {
  override def unapply(v: Option[Any]): Option[R] = v match {
    case Some(vv) if runtimeClass.isInstance(vv) => Some(vv.asInstanceOf[R])
    case _ => None
  }
}

object PString extends ParamSimple("string", classOf[String])

// Need to use the java (boxed) versions
object PNumber extends ParamSimple("number", classOf[java.lang.Double])

object PBoolean extends ParamSimple("boolean", classOf[java.lang.Boolean])

case class POption[T](p: CallParam[T]) extends CallParam[Option[T]] {
  override def name = "[%s]".format(p.name)
  override def unapply(v: Option[Any]) = v match {
    case None => Some(None)
    case p(vv) => Some(Some(vv))
    case _ => None
  }
}
