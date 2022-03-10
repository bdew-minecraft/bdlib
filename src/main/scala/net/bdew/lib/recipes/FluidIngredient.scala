package net.bdew.lib.recipes

import com.google.gson.{JsonElement, JsonObject, JsonSyntaxException}
import net.bdew.lib.{JSObj, JSObjectsArray, JSResLoc, JSSinglePair}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries

case class FluidIngredient(vals: Set[GenIngredient[Fluid]]) {
  def matches(f: Fluid): Boolean = vals.exists(_.test(f))
  def matches(fs: FluidStack): Boolean = matches(fs.getFluid)

  def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeVarInt(vals.size)
    vals.foreach(_.toPacket(pkt))
  }
}

object FluidIngredient {
  def singleFluid(js: JsonObject): Set[GenIngredient[Fluid]] = {
    js match {
      case JSSinglePair("fluid", JSResLoc(key)) =>
        if (!ForgeRegistries.FLUIDS.containsKey(key))
          throw new JsonSyntaxException(s"Fluid not found - $key")
        Set(GenIngredient.of(ForgeRegistries.FLUIDS.getValue(key)))
      case JSSinglePair("fluidTag", JSResLoc(key)) =>
        Set(GenIngredient.of(FluidTags.create(key)))
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

  def fromPacket(pkt: FriendlyByteBuf): FluidIngredient = {
    val count = pkt.readVarInt()
    FluidIngredient(
      (0 until count)
        .map(_ => GenIngredient.fromPacket[Fluid](pkt))
        .toSet
    )
  }
}