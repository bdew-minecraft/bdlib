package net.bdew.lib.power

import net.bdew.lib.Misc
import net.bdew.lib.config.PowerConfig
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.Constants

case class DataSlotPower(name: String, parent: DataSlotContainer) extends DataSlot {
  updateKind = Set(UpdateKind.GUI, UpdateKind.SAVE)

  var stored = 0F
  var capacity = 0F
  var maxReceive = 0F

  def inject(v: Float, simulate: Boolean): Float = {
    val canAdd = Misc.clamp(v, 0F, Misc.min(capacity - stored, maxReceive))
    if ((canAdd > 0) && !simulate) {
      stored += canAdd
      parent.dataSlotChanged(this)
    }
    canAdd
  }

  def extract(v: Float, simulate: Boolean): Float = {
    val canExtract = Misc.clamp(v, 0F, stored)
    if ((canExtract > 0) && !simulate) {
      stored -= canExtract
      if (stored < 1) stored = 0
      parent.dataSlotChanged(this)
    }
    canExtract
  }

  def configure(cfg: PowerConfig): Unit = {
    capacity = cfg.capacity()
    maxReceive = cfg.maxReceive()
  }

  def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    val tag = new CompoundNBT()
    tag.putFloat("stored", stored)
    if (kind == UpdateKind.GUI)
      tag.putFloat("capacity", capacity)
    t.put(name, tag)
  }

  def load(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    if (t.contains(name, Constants.NBT.TAG_COMPOUND)) {
      val tag = t.getCompound(name)
      stored = tag.getFloat("stored")
      if (kind == UpdateKind.GUI)
        capacity = tag.getFloat("capacity")
    }
  }
}


