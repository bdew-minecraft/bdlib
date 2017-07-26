/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.Misc
import net.bdew.lib.gui.{IconWrapper, Texture}
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
  * Allows loading of texture that don't belong to specific block or item into vanilla block texture atlas
  */
class IconPreloader {
  lazy val map = {
    (for {
      method <- getClass.getMethods if method.getParameterTypes.isEmpty && classOf[TextureLoc].isAssignableFrom(method.getReturnType)
      value <- Misc.asInstanceOpt(method.invoke(this), classOf[TextureLoc])
    } yield method.getName -> value).toMap
  }

  var icons = Set.empty[TextureLoc]

  case class TextureLoc(loc: String) {
    var texture: IconWrapper = _
    icons += this
  }

  import scala.language.implicitConversions

  implicit def entry2texture(v: TextureLoc): IconWrapper = if (v.texture != null) v.texture else sys.error("Accessing unloaded texture in IconPreloader")

  def registerIcons(reg: TextureMap) {}

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    for (x <- icons) x.texture = Texture(Texture.BLOCKS, ev.getMap.registerSprite(new ResourceLocation(x.loc)))
    registerIcons(ev.getMap)
  }
}
