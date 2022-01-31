package net.bdew.lib.resource

import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

case class Resource(kind: ResourceKind, amount: Double)

object Resource {
  def from(fs: FluidStack): Resource = Resource(FluidResource(fs.getFluid), fs.getAmount)
  def from(is: ItemStack): Resource = Resource(ItemResource(is.getItem), is.getCount)

  def loadFromNBT(tag: CompoundTag): Option[Resource] = {
    ResourceKind.loadFromNBT(tag).map(Resource(_, tag.getDouble("amount")))
  }

  def saveToNBT(r: Resource): CompoundTag = {
    NBT.from { tag =>
      ResourceKind.saveToNBT(tag, r.kind)
      tag.putDouble("amount", r.amount)
    }
  }
}