package net.bdew.lib.resource

import net.bdew.lib.data.base.{DataSlotContainer, UpdateKind}
import net.bdew.lib.data.mixins.DataSlotOption
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.Constants

case class DataSlotResourceKindOption(name: String, parent: DataSlotContainer) extends DataSlotOption[ResourceKind] {
  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    value foreach { r =>
      t.put(name, NBT.from { tag =>
        r.helperObject.saveToNBT(tag, r)
        tag.putString("kind", r.helperObject.id)
      })
    }
  }

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): Option[ResourceKind] = {
    if (t.contains(name, Constants.NBT.TAG_COMPOUND)) {
      val tag = t.getCompound(name)
      if (tag.contains("kind", Constants.NBT.TAG_STRING)) {
        ResourceManager.resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
      } else None
    } else None
  }
}
