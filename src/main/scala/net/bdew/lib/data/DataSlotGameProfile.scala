package net.bdew.lib.data

import com.mojang.authlib.GameProfile
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.{CompoundNBT, NBTUtil}
import net.minecraftforge.common.util.Constants

case class DataSlotGameProfile(name: String, parent: DataSlotContainer) extends DataSlotVal[GameProfile] {
  override val default: GameProfile = null

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): GameProfile = {
    if (t.contains(name, Constants.NBT.TAG_COMPOUND))
      NBTUtil.readGameProfile(t.getCompound(name))
    else
      null
  }

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    if (value != null) {
      t.put(name, NBTUtil.writeGameProfile(new CompoundNBT, value))
    }
  }
}
