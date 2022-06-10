package net.bdew.lib.recipes

import net.bdew.lib.inventory.NullInventory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{Recipe, RecipeSerializer, RecipeType}
import net.minecraft.world.level.Level
import net.minecraftforge.registries.RegistryObject

abstract class BaseMachineRecipe(val id: ResourceLocation) extends Recipe[NullInventory] {
  override def matches(inv: NullInventory, world: Level): Boolean = true
  override def assemble(inv: NullInventory): ItemStack = ItemStack.EMPTY
  override def canCraftInDimensions(x: Int, y: Int): Boolean = true
  override def getResultItem: ItemStack = ItemStack.EMPTY
  override def getId: ResourceLocation = id
  override def isSpecial: Boolean = true
}

abstract class BaseMachineRecipeSerializer[T <: BaseMachineRecipe] extends RecipeSerializer[T]

class MachineRecipeType[T <: BaseMachineRecipe](reg: RegistryObject[_ <: RecipeSerializer[T]]) extends RecipeType[T] {
  val registryName: ResourceLocation = reg.getId
  override def toString: String = registryName.toString
}