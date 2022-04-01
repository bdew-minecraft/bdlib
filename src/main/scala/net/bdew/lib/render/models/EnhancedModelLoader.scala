package net.bdew.lib.render.models

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.mojang.datafixers.util.Either
import net.bdew.lib.Client
import net.minecraft.client.resources.model.Material
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraftforge.client.model.IModelLoader

import scala.jdk.CollectionConverters._

class EnhancedModelLoader(val enhancer: ModelEnhancer) extends IModelLoader[EnhancedBlockModelGeometry] {
  private def material(res: ResourceLocation): Either[Material, String] =
    Either.left(new Material(Client.blocksAtlas, res))

  override def read(deserializationContext: JsonDeserializationContext, modelContents: JsonObject): EnhancedBlockModelGeometry = {
    val textures = modelContents.get("textures")
      .getAsJsonObject
      .entrySet()
      .asScala
      .map(x => x.getKey -> material(new ResourceLocation(x.getValue.getAsString)))
      .toMap.asJava

    val extraTex = if (modelContents.has("extraTex"))
      modelContents.get("extraTex")
        .getAsJsonObject
        .entrySet()
        .asScala
        .map(x => x.getKey -> new Material(Client.blocksAtlas, new ResourceLocation(x.getValue.getAsString)))
        .toMap
    else
      Map.empty[String, Material]

    new EnhancedBlockModelGeometry(enhancer, new ResourceLocation(modelContents.get("parent").getAsString), textures, extraTex)
  }

  override def onResourceManagerReload(resourceManager: ResourceManager): Unit = {
    //???
  }
}
