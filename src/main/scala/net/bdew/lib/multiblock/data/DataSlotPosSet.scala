package net.bdew.lib.multiblock.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag

import scala.collection.mutable

case class DataSlotPosSet(name: String, parent: DataSlotContainer) extends DataSlot {
  val set = collection.mutable.Set.empty[BlockPos]

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.setListVals(name, set.toList)
  }

  def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    set.clear()
    set ++= t.getListVals[BlockPos](name)
  }

  def updated(): Unit = parent.dataSlotChanged(this)
}

object DataSlotPosSet {

  import scala.language.implicitConversions

  implicit def dataSlotPosSet2set(v: DataSlotPosSet): mutable.Set[BlockPos] = v.set
}
