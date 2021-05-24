package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.fluids.{FluidStack, IFluidTank}

abstract class DataSlotTankBase(sz: Int) extends FluidTank(sz) with IFluidTank with IFluidHandler with DataSlot {
  updateKind = Set(UpdateKind.GUI, UpdateKind.SAVE)

  val sendCapacityOnUpdateKind = Set(UpdateKind.GUI)

  def save(t: CompoundNBT, k: UpdateKind.Value): Unit = {
    val tag = new CompoundNBT()
    writeToNBT(tag)
    if (sendCapacityOnUpdateKind.contains(k))
      tag.putInt("size", getCapacity)
    t.put(name, tag)
  }

  def load(t: CompoundNBT, k: UpdateKind.Value): Unit = {
    setFluid(null)
    val tag = t.getCompound(name)
    readFromNBT(tag)
    if (sendCapacityOnUpdateKind.contains(k))
      setCapacity(tag.getInt("size"))
  }

  override def setFluid(fluid: FluidStack): Unit = execWithChangeNotify(super.setFluid(fluid))
  override def setCapacity(capacity: Int): FluidTank = execWithChangeNotify(super.setCapacity(capacity))

  override def onContentsChanged(): Unit = parent.dataSlotChanged(this)
}

case class DataSlotTank(name: String, parent: DataSlotContainer, size: Int) extends DataSlotTankBase(size) {
}

case class DataSlotTankRestricted(name: String, parent: DataSlotContainer, var size: Int, filterFluid: Fluid) extends DataSlotTankBase(size) {
  override def isFluidValid(stack: FluidStack): Boolean = stack.getFluid == filterFluid
}
