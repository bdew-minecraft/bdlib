package net.bdew.lib.recipes

import net.bdew.lib.{BdLib, Event, Event1}
import net.minecraft.server.packs.resources.{ResourceManager, SimplePreparableReloadListener}
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.event.{AddReloadListenerEvent, TagsUpdatedEvent}
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = BdLib.ModId)
object RecipeReloadListener {
  var serverRecipeManager: RecipeManager = _
  var clientRecipeManager: RecipeManager = _

  val onClientRecipeUpdate: Event1[RecipeManager] = Event[RecipeManager]
  val onServerRecipeUpdate: Event1[RecipeManager] = Event[RecipeManager]

  @SubscribeEvent
  def addReloadListener(event: AddReloadListenerEvent): Unit = {
    event.addListener(
      new SimplePreparableReloadListener[Void]() {
        override protected def prepare(manager: ResourceManager, profiler: ProfilerFiller): Void = {
          serverRecipeManager = event.getDataPackRegistries.getRecipeManager
          null
        }

        override protected def apply(nothing: Void, resourceManagerIn: ResourceManager, profilerIn: ProfilerFiller): Unit = {
        }
      })
  }

  @SubscribeEvent
  def tagsUpdated(event: TagsUpdatedEvent): Unit = {
    if (serverRecipeManager != null)
      onServerRecipeUpdate.trigger(serverRecipeManager)
  }

  @SubscribeEvent
  def recipesUpdated(event: RecipesUpdatedEvent): Unit = {
    clientRecipeManager = event.getRecipeManager
    onClientRecipeUpdate.trigger(clientRecipeManager)
  }
}
