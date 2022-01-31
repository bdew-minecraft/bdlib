package net.bdew.lib.data

import com.mojang.authlib.GameProfile
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.{CompoundTag, NbtUtils, Tag}

case class DataSlotGameProfile(name: String, parent: DataSlotContainer) extends DataSlotVal[GameProfile] {
  override val default: GameProfile = null

  override def loadValue(t: CompoundTag, kind: UpdateKind.Value): GameProfile = {
    if (t.contains(name, Tag.TAG_COMPOUND))
      NbtUtils.readGameProfile(t.getCompound(name))
    else
      null
  }

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    if (value != null) {
      t.put(name, NbtUtils.writeGameProfile(new CompoundTag, value))
    }
  }
}
