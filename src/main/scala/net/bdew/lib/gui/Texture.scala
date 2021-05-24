/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.Client
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation

trait Texture {
  def u1: Float
  def u2: Float
  def v1: Float
  def v2: Float
  def bind(): Unit
}

class InterpolatedTexture(val texture: Texture, rect: Rect) extends Texture {
  def bind(): Unit = texture.bind()
  def u1: Float = rect.x1 * (texture.u2 - texture.u1) + texture.u1
  def u2: Float = rect.x2 * (texture.u2 - texture.u1) + texture.u1
  def v1: Float = rect.y1 * (texture.v2 - texture.v1) + texture.v1
  def v2: Float = rect.y2 * (texture.v2 - texture.v1) + texture.v1
}

class IconWrapper(val resource: ResourceLocation, icon: TextureAtlasSprite) extends Texture {
  def u1: Float = icon.getU0
  def u2: Float = icon.getU1
  def v1: Float = icon.getV0
  def v2: Float = icon.getV1
  def bind(): Unit = Client.textureManager.bind(resource)
}

class Sprite(val resource: ResourceLocation, r: Rect, scale: Int = 256) extends Texture {
  val rect: BaseRect[Float] = Rect(r.x / scale, r.y / scale, r.w / scale, r.h / scale)
  def u1: Float = rect.x1
  def u2: Float = rect.x2
  def v1: Float = rect.y1
  def v2: Float = rect.y2
  def bind(): Unit = Client.textureManager.bind(resource)
}

class ScaledResourceLocation(path: String, val scale: Int = 256) extends ResourceLocation(path) {
  def this(domain: String, path: String, scale: Int) = this(domain + ":" + path, scale)
  def this(domain: String, path: String) = this(domain + ":" + path, 256)
}

object Texture {
  // From minecraft sprite
  def apply(res: ResourceLocation, i: TextureAtlasSprite) = new IconWrapper(res, i)

  // From a ResourceLocation - use full file
  def apply(res: ResourceLocation): Sprite =
    if (res.getPath.endsWith(".png"))
      new Sprite(res, Rect(0, 0, 1, 1), 1)
    else // If it's not a full file name, add base path and extension like TextureMap does
      new Sprite(new ResourceLocation(res.getNamespace, "textures/" + res.getPath + ".png"), Rect(0, 0, 1, 1), 1)

  def apply(path: String): Texture = apply(new ResourceLocation(path))

  // From a ResourceLocation and position, scale optional
  def apply(res: ResourceLocation, r: Rect) = new Sprite(res, r)
  def apply(res: ResourceLocation, r: Rect, scale: Int) = new Sprite(res, r, scale)
  def apply(res: ResourceLocation, u1: Float, v1: Float, w: Float, h: Float) = new Sprite(res, Rect(u1, v1, w, h))
  def apply(res: ResourceLocation, u1: Float, v1: Float, w: Float, h: Float, scale: Int) = new Sprite(res, Rect(u1, v1, w, h), scale)

  // From a path (with optional domain) and position, no scale
  def apply(path: String, r: Rect) = new Sprite(new ResourceLocation(path), r)
  def apply(path: String, u1: Float, v1: Float, w: Float, h: Float) = new Sprite(new ResourceLocation(path), Rect(u1, v1, w, h))
  def apply(domain: String, path: String, r: Rect) = new Sprite(new ResourceLocation(domain, path), r)
  def apply(domain: String, path: String, u1: Float, v1: Float, w: Float, h: Float) = new Sprite(new ResourceLocation(domain, path), Rect(u1, v1, w, h))

  // From a path (with optional domain), position and scale
  def apply(path: String, r: Rect, scale: Int) = new Sprite(new ResourceLocation(path), r, scale)
  def apply(path: String, u1: Float, v1: Float, w: Float, h: Float, scale: Int) = new Sprite(new ResourceLocation(path), Rect(u1, v1, w, h), scale)
  def apply(domain: String, path: String, r: Rect, scale: Int) = new Sprite(new ResourceLocation(domain, path), r, scale)
  def apply(domain: String, path: String, u1: Float, v1: Float, w: Float, h: Float, scale: Int) = new Sprite(new ResourceLocation(domain, path), Rect(u1, v1, w, h), scale)

  // From a ScaledResourceLocation and position
  def apply(res: ScaledResourceLocation, r: Rect) = new Sprite(res, r, res.scale)
  def apply(res: ScaledResourceLocation, u1: Float, v1: Float, w: Float, h: Float) = new Sprite(res, Rect(u1, v1, w, h), res.scale)

  // A fraction of another Texture, parameters should be between 0 and 1
  def interpolate(texture: Texture, rect: Rect) = new InterpolatedTexture(texture, rect)
  def interpolate(texture: Texture, u1: Float, v1: Float, u2: Float, v2: Float) = new InterpolatedTexture(texture, Rect(u1, v1, u2 - u1, v2 - v1))

  def block(res: ResourceLocation): IconWrapper =
    Texture(Client.blocksAtlas, Client.minecraft.getTextureAtlas(Client.blocksAtlas).apply(res))

  def block(s: String): IconWrapper =
    Texture(Client.blocksAtlas, Client.minecraft.getTextureAtlas(Client.blocksAtlas).apply(new ResourceLocation(s)))

  def block(sprite: TextureAtlasSprite): IconWrapper =
    Texture(Client.blocksAtlas, sprite)
}
