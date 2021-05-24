package net.bdew.lib.render

import net.bdew.lib.gui.{IconWrapper, Texture}
import net.bdew.lib.{Client, Misc}
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Allows loading of texture that don't belong to specific block or item into vanilla block texture atlas
 * needs annotation on final object: @Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = ..., bus = Bus.MOD)
 */
class IconPreloader {
  lazy val map: Map[String, TextureLoc] = {
    (for {
      method <- getClass.getMethods if method.getParameterTypes.isEmpty && classOf[TextureLoc].isAssignableFrom(method.getReturnType)
      value <- Misc.asInstanceOpt(method.invoke(this), classOf[TextureLoc])
    } yield method.getName -> value).toMap
  }

  var icons = Set.empty[TextureLoc]

  case class TextureLoc(loc: String) {
    val resource: ResourceLocation = new ResourceLocation(loc)
    var texture: IconWrapper = _
    icons += this
  }

  import scala.language.implicitConversions

  implicit def entry2texture(v: TextureLoc): IconWrapper =
    if (v.texture != null) v.texture else sys.error("Accessing unloaded texture in IconPreloader")

  def registerIcons(reg: AtlasTexture): Unit = {}

  @SubscribeEvent
  def preTextureStitch(ev: TextureStitchEvent.Pre): Unit = {
    if (ev.getMap.location() == Client.blocksAtlas) {
      icons.foreach(icon => ev.addSprite(icon.resource))
    }
    registerIcons(ev.getMap)
  }

  @SubscribeEvent
  def postTextureStitch(ev: TextureStitchEvent.Post): Unit = {
    if (ev.getMap.location() == Client.blocksAtlas) {
      icons.foreach(icon => icon.texture = Texture(Client.blocksAtlas, ev.getMap.getSprite(icon.resource)))
    }
  }
}
