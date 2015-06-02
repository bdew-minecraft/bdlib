/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.bdew.lib.Misc
import net.bdew.lib.gui.{IconWrapper, Texture}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge

/**
 * Allows loading of texture that don't belong to specific block or item into vanilla block/item atlas
 * @param kind 0=Blocks / 1=Items
 */
class IconPreloader(kind: Int) {
  lazy val map = {
    (for {
      method <- getClass.getMethods if method.getParameterTypes.isEmpty && classOf[TextureLoc].isAssignableFrom(method.getReturnType)
      value <- Misc.asInstanceOpt(method.invoke(this), classOf[TextureLoc])
    } yield method.getName -> value).toMap
  }

  var icons = Set.empty[TextureLoc]

  val sheet = kind match {
    case 0 => Texture.BLOCKS
    case 1 => Texture.ITEMS
    case x => sys.error("Invalid sprite sheet number: %d".format(x))
  }

  case class TextureLoc(loc: String) {
    var texture: IconWrapper = null
    icons += this
  }

  import scala.language.implicitConversions

  implicit def entry2icon(v: TextureLoc): IIcon = v.texture.icon
  implicit def entry2texture(v: TextureLoc): IconWrapper = v.texture

  def registerIcons(reg: IIconRegister) {}

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == kind) {
      for (x <- icons) x.texture = Texture(sheet, ev.map.registerIcon(x.loc))
      registerIcons(ev.map)
    }
  }
}
