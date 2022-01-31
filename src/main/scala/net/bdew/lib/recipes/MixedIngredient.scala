package net.bdew.lib.recipes

import com.google.gson.{JsonObject, JsonSyntaxException}
import net.bdew.lib.resource.{FluidResource, ItemResource, Resource}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.fluids.FluidStack

trait MixedIngredient {
  def matches(is: ItemStack): Boolean
  def matches(fs: FluidStack): Boolean
  def matches(res: Resource): Boolean
  def toPacket(pkt: FriendlyByteBuf): Unit
}

object MixedIngredient {
  case class Item(ingredient: Ingredient) extends MixedIngredient {
    override def matches(is: ItemStack): Boolean = ingredient.test(is)
    override def matches(fs: FluidStack): Boolean = false

    override def matches(res: Resource): Boolean = res match {
      case Resource(i: ItemResource, _) => ingredient.test(i.makeStack(1))
      case _ => false
    }

    override def toPacket(pkt: FriendlyByteBuf): Unit = {
      pkt.writeUtf("item")
      ingredient.toNetwork(pkt)
    }
  }

  case class Fluid(ingredient: FluidIngredient) extends MixedIngredient {
    override def matches(is: ItemStack): Boolean = false
    override def matches(fs: FluidStack): Boolean = ingredient.matches(fs)
    override def matches(res: Resource): Boolean = res match {
      case Resource(i: FluidResource, _) => ingredient.matches(i.fluid)
      case _ => false
    }
    override def toPacket(pkt: FriendlyByteBuf): Unit = {
      pkt.writeUtf("fluid")
      ingredient.toPacket(pkt)
    }
  }

  def apply(v: Ingredient): MixedIngredient = Item(v)
  def apply(v: FluidIngredient): MixedIngredient = Fluid(v)

  def fromPacket(pkt: FriendlyByteBuf): MixedIngredient = {
    pkt.readUtf() match {
      case "item" => Item(Ingredient.fromNetwork(pkt))
      case "fluid" => Fluid(FluidIngredient.fromPacket(pkt))
      case x => throw new RuntimeException(s"Unknown ingredient type $x")
    }
  }

  def fromJson(js: JsonObject): MixedIngredient = {
    if (js.has("items"))
      Item(Ingredient.fromJson(js.get("items")))
    else if (js.has("fluids"))
      Fluid(FluidIngredient.fromJson(js.get("fluids")))
    else throw new JsonSyntaxException("Expected fluids or items ingredient")
  }
}