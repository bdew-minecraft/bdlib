package net.bdew.lib.resource

import net.bdew.lib.Text
import net.bdew.lib.gui.{Color, Texture}
import net.minecraft.nbt.{CompoundTag, Tag}
import net.minecraft.network.chat.{Component, MutableComponent}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.registries.ForgeRegistries

case class FluidResource(fluid: Fluid) extends ResourceKind {
  override def getTexture: Texture = Texture.block(IClientFluidTypeExtensions.of(fluid).getStillTexture)
  override def getColor: Color = Color.fromInt(IClientFluidTypeExtensions.of(fluid).getTintColor)
  override def getName: Component = fluid.getFluidType.getDescription
  override def getFormattedString(amount: Double, capacity: Double): MutableComponent =
    Text.fluidCap(amount, capacity)
  override def capacityMultiplier = 1
  override def toString: String = "FluidResource(%s)".format(ForgeRegistries.FLUIDS.getKey(fluid))
  override def helperObject: ResourceHelper[_ >: FluidResource.this.type] = FluidResourceHelper
}

object FluidResourceHelper extends ResourceHelper[FluidResource]("fluid") {
  override def loadFromNBT(tag: CompoundTag): Option[FluidResource] = {
    if (tag.contains("fluid", Tag.TAG_STRING)) {
      val fName = tag.getString("fluid")
      Option(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fName))) map FluidResource
    } else None
  }
  override def saveToNBT(tag: CompoundTag, r: FluidResource): Unit = {
    tag.putString("fluid", ForgeRegistries.FLUIDS.getKey(r.fluid).toString)
  }
}
