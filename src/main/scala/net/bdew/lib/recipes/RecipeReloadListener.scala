package net.bdew.lib.recipes

import net.bdew.lib.{BdLib, Event, Event1}
import net.minecraft.client.resources.ReloadListener
import net.minecraft.item.crafting.RecipeManager
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.event.{AddReloadListenerEvent, TagsUpdatedEvent}
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent

@Mod.EventBusSubscriber(modid = BdLib.ModId)
object RecipeReloadListener {
  var serverRecipeManager: RecipeManager = _
  var clientRecipeManager: RecipeManager = _

  val onClientRecipeUpdate: Event1[RecipeManager] = Event[RecipeManager]
  val onServerRecipeUpdate: Event1[RecipeManager] = Event[RecipeManager]

  @SubscribeEvent
  def addReloadListener(event: AddReloadListenerEvent): Unit = {
    event.addListener(
      new ReloadListener[Void]() {
        override protected def prepare(manager: IResourceManager, profiler: IProfiler): Void = {
          serverRecipeManager = event.getDataPackRegistries.getRecipeManager
          null
        }

        override protected def apply(nothing: Void, resourceManagerIn: IResourceManager, profilerIn: IProfiler): Unit = {
        }
      })
  }

  @SubscribeEvent def tagsUpdated(event: TagsUpdatedEvent.VanillaTagTypes): Unit = {
    if (serverRecipeManager != null)
      onServerRecipeUpdate.trigger(serverRecipeManager)
  }

  @SubscribeEvent def idRemap(event: FMLModIdMappingEvent): Unit = {
    if (serverRecipeManager != null)
      onServerRecipeUpdate.trigger(serverRecipeManager)
  }

  @SubscribeEvent
  def recipesUpdated(event: RecipesUpdatedEvent): Unit = {
    clientRecipeManager = event.getRecipeManager
    onClientRecipeUpdate.trigger(clientRecipeManager)
  }
}
