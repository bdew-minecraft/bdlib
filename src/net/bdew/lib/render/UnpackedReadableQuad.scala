/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.render.primitive._
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.pipeline.{IVertexConsumer, UnpackedBakedQuad}

trait ReadableQuad extends UnpackedBakedQuad {
  lazy val helper = VertexFormatHelper.get(getFormat)

  def getUnpackedData: Array[Array[Array[Float]]]
  def getFormat: VertexFormat

  def getPosition(i: Int) = {
    val v = getUnpackedData(i)(helper.positionIndex)
    Vertex(v(0), v(1), v(2))
  }

  def getTexture(i: Int) = {
    val v = getUnpackedData(i)(helper.textureIndex)
    UV(v(0), v(1))
  }

  def getVertex(i: Int) = {
    val v = getUnpackedData(i)
    val p = v(helper.positionIndex)
    val t = v(helper.textureIndex)
    TVertex(p(0), p(1), p(2), t(0), t(1))
  }

  def getQuad(shaded: Boolean = true) = {
    TQuad(List4.from(getVertex), getFace, getSprite, getTintIndex, shaded, shouldApplyDiffuseLighting())
  }
}

/**
  * UnpackedBakedQuad subclass with accessible data
  */
class UnpackedReadableQuad(unpackedData: Array[Array[Array[Float]]], tint: Int, orientation: EnumFacing, texture: TextureAtlasSprite, applyDiffuseLighting: Boolean, format: VertexFormat) extends UnpackedBakedQuad(unpackedData, tint, orientation, texture, applyDiffuseLighting, format) with ReadableQuad {
  def getUnpackedData = unpackedData
}

/**
  * Like UnpackedBakedQuad.Builder, but outputs readable quads
  */
class Unpacker(format: VertexFormat) extends IVertexConsumer {
  var unpackedData = Array.fill(4, format.getElementCount, 4)(0f)
  var tint = -1
  var orientation: EnumFacing = _
  var vertices = 0
  var elements = 0
  var full = false
  var texture: TextureAtlasSprite = _
  var applyDiffuseLighting = false

  def reset(): Unit = {
    unpackedData = Array.fill(4, format.getElementCount, 4)(0f)
    tint = -1
    orientation = null
    vertices = 0
    elements = 0
    full = false
    texture = null
    applyDiffuseLighting = false
  }

  override def getVertexFormat: VertexFormat = {
    return format
  }

  override def setQuadTint(tint: Int) {
    this.tint = tint
  }

  override def setQuadOrientation(orientation: EnumFacing) {
    this.orientation = orientation
  }

  def setTexture(texture: TextureAtlasSprite) {
    this.texture = texture
  }

  override def setApplyDiffuseLighting(diffuse: Boolean) {
    this.applyDiffuseLighting = diffuse
  }

  override def put(element: Int, data: Float*) {
    for (i <- 0 until 4)
      unpackedData(vertices)(element)(i) = if (i < data.length) data(i) else 0
    elements += 1
    if (elements == format.getElementCount) {
      vertices += 1
      elements = 0
    }
    if (vertices == 4) {
      full = true
    }
  }

  def build(): ReadableQuad = {
    if (!full) throw new IllegalStateException("not enough data")
    return new UnpackedReadableQuad(unpackedData, tint, orientation, texture, applyDiffuseLighting, format)
  }

  def unpack(quad: BakedQuad): ReadableQuad = {
    reset()
    applyDiffuseLighting = quad.shouldApplyDiffuseLighting()
    texture = quad.getSprite
    quad.pipe(this)
    build()
  }
}