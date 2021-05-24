package net.bdew.lib.render.models

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.mojang.datafixers.util.Either
import net.bdew.lib.Client
import net.minecraft.client.renderer.model.RenderMaterial
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelLoader

import scala.jdk.CollectionConverters._

class EnhancedModelLoader(val enhancer: ModelEnhancer) extends IModelLoader[EnhancedBlockModelGeometry] {
  private def material(res: ResourceLocation): Either[RenderMaterial, String] =
    Either.left(new RenderMaterial(Client.blocksAtlas, res))

  override def read(deserializationContext: JsonDeserializationContext, modelContents: JsonObject): EnhancedBlockModelGeometry = {
    new EnhancedBlockModelGeometry(enhancer, new ResourceLocation(modelContents.get("parent").getAsString),
      modelContents.get("textures")
        .getAsJsonObject
        .entrySet()
        .asScala
        .map(x => x.getKey -> material(new ResourceLocation(x.getValue.getAsString)))
        .toMap.asJava
    )
  }

  override def onResourceManagerReload(resourceManager: IResourceManager): Unit = {
    //???
  }
}
