package net.bdew.lib.recipes

import com.google.gson.{JsonElement, JsonObject, JsonSyntaxException}
import net.bdew.lib.{JSObj, JSObjectsArray, JSResLoc, JSSinglePair}
import net.minecraft.fluid.Fluid
import net.minecraft.network.PacketBuffer
import net.minecraft.tags.FluidTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries

import scala.jdk.CollectionConverters._

case class FluidIngredient(fluids: Set[Fluid]) {
  def matches(f: Fluid): Boolean = fluids.exists(_.isSame(f))
  def matches(fs: FluidStack): Boolean = matches(fs.getFluid)

  def toPacket(pkt: PacketBuffer): Unit = {
    pkt.writeVarInt(fluids.size)
    fluids.foreach(x => pkt.writeUtf(x.getRegistryName.toString))
  }
}

object FluidIngredient {
  def singleFluid(js: JsonObject): Set[Fluid] = {
    js match {
      case JSSinglePair("fluid", JSResLoc(key)) =>
        if (!ForgeRegistries.FLUIDS.containsKey(key))
          throw new JsonSyntaxException(s"Fluid not found - $key")
        Set(ForgeRegistries.FLUIDS.getValue(key))
      case JSSinglePair("fluidTag", JSResLoc(key)) =>
        if (!FluidTags.getAllTags.getAllTags.containsKey(key))
          throw new JsonSyntaxException(s"Fluid tag not found - $key")
        FluidTags.getAllTags.getTag(key).getValues.asScala.toSet
      case _ => throw new JsonSyntaxException(s"Invalid fluid ingredient: ${js.toString}")
    }
  }

  def fromJson(js: JsonElement): FluidIngredient = {
    js match {
      case JSObj(o) => FluidIngredient(singleFluid(o))
      case JSObjectsArray(a) => FluidIngredient(a.flatMap(singleFluid).toSet)
      case _ => throw new JsonSyntaxException("Expected fluid ingredient to be object or array of objects")
    }
  }

  def fromPacket(pkt: PacketBuffer): FluidIngredient = {
    val count = pkt.readVarInt()
    FluidIngredient(
      (0 until count)
        .map(_ => ForgeRegistries.FLUIDS.getValue(new ResourceLocation(pkt.readUtf())))
        .toSet
    )
  }
}