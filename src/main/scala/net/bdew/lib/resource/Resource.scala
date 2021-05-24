package net.bdew.lib.resource

import net.bdew.lib.nbt.NBT
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fluids.FluidStack

case class Resource(kind: ResourceKind, amount: Double)

object Resource {
  def from(fs: FluidStack): Resource = Resource(FluidResource(fs.getFluid), fs.getAmount)
  def from(is: ItemStack): Resource = Resource(ItemResource(is.getItem), is.getCount)

  def loadFromNBT(tag: CompoundNBT): Option[Resource] = {
    ResourceKind.loadFromNBT(tag).map(Resource(_, tag.getDouble("amount")))
  }

  def saveToNBT(r: Resource): CompoundNBT = {
    NBT.from { tag =>
      ResourceKind.saveToNBT(tag, r.kind)
      tag.putDouble("amount", r.amount)
    }
  }
}