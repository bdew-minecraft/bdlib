/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.render.primitive.{TQuad, TVertex}
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.vertex.{DefaultVertexFormats, VertexFormat, VertexFormatElement}
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.pipeline.{LightUtil, UnpackedBakedQuad}

import scala.collection.JavaConversions._

class QuadBaker(format: VertexFormat) {
  def bakeList(quads: List[TQuad]): List[BakedQuad] = quads map bakeQuad
  def bakeMap[T](quads: Map[T, TQuad]): Map[T, BakedQuad] = quads mapValues bakeQuad
  def bakeListMap[T](quads: Map[T, List[TQuad]]): Map[T, List[BakedQuad]] = quads mapValues bakeList

  def bakeQuad(quad: TQuad): BakedQuad = {
    val array = new Array[Array[Array[Float]]](4)
    quad.vertexes.mapIntoArray(array, x => vertexToRaw(x, quad.face, quad.shading))
    new UnpackedBakedQuad(array, quad.tint, quad.face, quad.sprite, quad.diffuseLighting, format)
  }

  private def vertexToRaw(v: TVertex, face: EnumFacing, shading: Boolean): Array[Array[Float]] = {
    val unpackedData = new Array[Array[Float]](format.getElementCount)
    val light = if (shading) LightUtil.diffuseLight(face) else 1f
    for ((element, index) <- format.getElements.zipWithIndex) {
      unpackedData(index) = element.getUsage match {
        case VertexFormatElement.EnumUsage.POSITION => Array(v.x, v.y, v.z, 0)
        case VertexFormatElement.EnumUsage.UV => Array(v.u, v.v, 0, 0)
        case VertexFormatElement.EnumUsage.COLOR => Array(light, light, light, 1)
        case VertexFormatElement.EnumUsage.NORMAL => QuadBaker.normals(face)
        case _ => Array(0f, 0f, 0f, 0f)
      }
    }
    unpackedData
  }
}

object QuadBaker {
  private lazy val normals = EnumFacing.values().map(f => {
    f -> Array[Float](f.getFrontOffsetX, f.getFrontOffsetY, f.getFrontOffsetZ, 0)
  }).toMap
}

object QuadBakerDefault extends QuadBaker(DefaultVertexFormats.ITEM)