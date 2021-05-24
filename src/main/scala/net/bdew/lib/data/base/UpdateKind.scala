package net.bdew.lib.data.base

object UpdateKind extends Enumeration {
  /**
   * Changes should propagate to clients
   */
  val WORLD: UpdateKind.Value = Value("WORLD")

  /**
   * Changes should propagate to clients that have the GUI open
   */
  val GUI: UpdateKind.Value = Value("GUI")

  /**
   * Changes should be saved to persistent storage
   */
  val SAVE: UpdateKind.Value = Value("SAVE")

  /**
   * Changes should update model data on clients
   */
  val MODEL_DATA: UpdateKind.Value = Value("RENDER")

  /**
   * Changes should cause a render update on clients
   */
  val RENDER: UpdateKind.Value = Value("RENDER")

  /**
   * Changes should cause a block update
   */
  val UPDATE: UpdateKind.Value = Value("UPDATE")
}