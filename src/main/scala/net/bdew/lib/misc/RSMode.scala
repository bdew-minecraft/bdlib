package net.bdew.lib.misc

object RSMode extends Enumeration {
  val ALWAYS: RSMode.Value = Value(0, "ALWAYS")
  val RS_ON: RSMode.Value = Value(1, "RS_ON")
  val RS_OFF: RSMode.Value = Value(2, "RS_OFF")
  val NEVER: RSMode.Value = Value(3, "NEVER")
}
