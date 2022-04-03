package net.bdew.lib.managers

import net.bdew.lib.inventory.NullInventory
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer, MachineRecipeType}
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.crafting
import net.minecraft.world.item.crafting.{Recipe, RecipeSerializer, RecipeType}
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

import scala.jdk.CollectionConverters._

class RecipeDef[C <: Container, T <: Recipe[C], S <: RecipeSerializer[T], R <: RecipeType[T]](val id: ResourceLocation, val serializerRef: RegistryObject[S], val recipeTypeRef: RegistryObject[R]) {
  def serializer: S = serializerRef.get
  def recipeType: R = recipeTypeRef.get
  def from(mgr: crafting.RecipeManager): List[T] = mgr.getAllRecipesFor[C, T](recipeType).asScala.toList
}

class MachineRecipeDef[T <: BaseMachineRecipe, S <: BaseMachineRecipeSerializer[T]](id: ResourceLocation, serializerRef: RegistryObject[S], recipeTypeRef: RegistryObject[MachineRecipeType[T]])
  extends RecipeDef[NullInventory, T, S, MachineRecipeType[T]](id, serializerRef, recipeTypeRef)

class RecipeManager extends RegistryManager(ForgeRegistries.RECIPE_SERIALIZERS) {
  val recipeTypes = new VanillaRegistryManager(Registry.RECIPE_TYPE)

  def register[C <: Container, T <: Recipe[C], S <: RecipeSerializer[T], R <: RecipeType[T]](id: String, serializerFactory: () => S, typeFactory: RegistryObject[S] => R): RecipeDef[C, T, S, R] = {
    val serializer = register(id, serializerFactory)
    val recipeType = recipeTypes.register(id, () => typeFactory(serializer))
    new RecipeDef(serializer.getId, serializer, recipeType)
  }

  def registerMachine[T <: BaseMachineRecipe, S <: BaseMachineRecipeSerializer[T]](id: String, serializerFactory: () => S): MachineRecipeDef[T, S] = {
    val serializer = register(id, serializerFactory)
    val recipeType = recipeTypes.register(id, () => new MachineRecipeType(serializer))
    new MachineRecipeDef(serializer.getId, serializer, recipeType)
  }

  override def init(): Unit = {
    super.init()
    recipeTypes.init()
  }
}
