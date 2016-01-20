/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util

import com.google.common.collect.ImmutableList
import net.bdew.lib.Client
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemCameraTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.{IFlexibleBakedModel, ISmartBlockModel}

import scala.collection.mutable

case class SimpleBakedModel(generalQuads: util.List[BakedQuad],
                            faceQuads: Map[EnumFacing, util.List[BakedQuad]],
                            texture: TextureAtlasSprite,
                            format: VertexFormat,
                            isAmbientOcclusion: Boolean,
                            isGui3d: Boolean,
                            isBuiltInRenderer: Boolean
                           ) extends IFlexibleBakedModel {
  override def getFormat = format
  override def getParticleTexture = texture
  override def getGeneralQuads = generalQuads
  override def getFaceQuads(face: EnumFacing) = faceQuads(face)
  override def getItemCameraTransforms = ItemCameraTransforms.DEFAULT
}

class SimpleBakedModelBuilder {
  val faceQuads = EnumFacing.values().map(f => f -> mutable.ListBuffer.empty[BakedQuad]).toMap
  var generalQuads = mutable.ListBuffer.empty[BakedQuad]
  var texture: TextureAtlasSprite = Client.missingIcon
  var format: VertexFormat = null
  var isAmbientOcclusion = true
  var isGui3d = false
  var isBuiltInRenderer = false

  def addQuad(face: EnumFacing, quad: BakedQuad) = faceQuads(face) += quad

  def addQuads(face: EnumFacing, quads: List[BakedQuad]) = faceQuads(face) ++= quads

  def addQuadMap(map: Map[EnumFacing, BakedQuad]) =
    for ((face, quad) <- map) faceQuads(face) += quad

  def addQuadListMap(map: Map[EnumFacing, List[BakedQuad]]) =
    for ((face, quads) <- map) faceQuads(face) ++= quads

  def addQuadGeneral(face: EnumFacing, quad: BakedQuad) = generalQuads += quad

  def addQuadsGeneral(face: EnumFacing, quads: List[BakedQuad]) = generalQuads ++= quads

  def build(): IFlexibleBakedModel = {
    import scala.collection.JavaConversions._
    if (format == null) throw new RuntimeException("Format not specified")
    new SimpleBakedModel(
      generalQuads = ImmutableList.copyOf(generalQuads.toIterable),
      faceQuads = faceQuads.mapValues(x => ImmutableList.copyOf(x.toIterable)),
      texture = texture,
      format = format,
      isAmbientOcclusion = isAmbientOcclusion,
      isGui3d = isGui3d,
      isBuiltInRenderer = isBuiltInRenderer
    )
  }
}

class SmartBakedModelBuilder extends SimpleBakedModelBuilder {
  var logic = List.empty[(IFlexibleBakedModel, IBlockState) => IFlexibleBakedModel]

  def addLogic(f: (IFlexibleBakedModel, IBlockState) => IFlexibleBakedModel) = logic :+= f

  override def build() = {
    val base = super.build()
    new FlexibleBakedModelProxy(base) with ISmartBlockModel {
      override def handleBlockState(state: IBlockState) =
        logic.foldLeft((base, state))((s, f) => f.tupled(s) -> s._2)._1
    }
  }
}