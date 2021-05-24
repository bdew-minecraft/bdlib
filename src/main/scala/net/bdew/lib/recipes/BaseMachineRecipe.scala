package net.bdew.lib.recipes

import net.bdew.lib.inventory.NullInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer, IRecipeType, RecipeManager}
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistryEntry

import scala.jdk.CollectionConverters._

abstract class BaseMachineRecipe(val id: ResourceLocation) extends IRecipe[NullInventory] {
  override def matches(inv: NullInventory, world: World): Boolean = true
  override def assemble(inv: NullInventory): ItemStack = ItemStack.EMPTY
  override def canCraftInDimensions(x: Int, y: Int): Boolean = true
  override def getResultItem: ItemStack = ItemStack.EMPTY
  override def getId: ResourceLocation = id
}

abstract class BaseMachineRecipeSerializer[T <: BaseMachineRecipe] extends ForgeRegistryEntry[IRecipeSerializer[_]] with IRecipeSerializer[T]

class MachineRecipeType[T <: BaseMachineRecipe](reg: RegistryObject[_ <: IRecipeSerializer[T]]) extends IRecipeType[T] {
  val registryName: ResourceLocation = reg.getId

  Registry.register(Registry.RECIPE_TYPE, registryName, this)

  override def toString: String = registryName.toString

  def getAllRecipes(mgr: RecipeManager): List[T] = mgr.getAllRecipesFor[NullInventory, T](this).asScala.toList
}