package net.bdew.lib.inventory

class SimpleInventory(size: Int) extends BaseInventory {
  override def getContainerSize: Int = size
  override def setChanged(): Unit = {}
}
