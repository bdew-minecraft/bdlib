/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util
import javax.vecmath.Matrix4f

import net.bdew.lib.render.QuadBaker
import net.bdew.lib.render.primitive.TQuad
import net.bdew.lib.{Client, Misc}
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.{BakedQuad, IBakedModel, ItemCameraTransforms, ItemOverrideList}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.{DefaultVertexFormats, VertexFormat}
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.model.{IModelState, TRSRTransformation}
import org.apache.commons.lang3.tuple.Pair

import scala.collection.mutable

case class SimpleBakedModel(generalQuads: util.List[BakedQuad],
                            faceQuads: Map[EnumFacing, util.List[BakedQuad]],
                            texture: TextureAtlasSprite,
                            format: VertexFormat,
                            transforms: (IBakedModel, TransformType) => (IBakedModel, Matrix4f),
                            processing: ModelProcessing.Processor,
                            itemOverrideList: ItemOverrideList,
                            isAmbientOcclusion: Boolean,
                            isGui3d: Boolean,
                            isBuiltInRenderer: Boolean
                           ) extends IBakedModel {

  override def getOverrides: ItemOverrideList = itemOverrideList

  override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] =
    processing(state, side, rand, if (side == null) generalQuads else faceQuads(side))

  override def getParticleTexture = texture
  override def getItemCameraTransforms = ItemCameraTransforms.DEFAULT
  override def handlePerspective(cameraTransformType: TransformType) = {
    val (model, matrix) = transforms(this, cameraTransformType)
    Pair.of(model, matrix)
  }
}

object ModelProcessing {
  type Processor = (IBlockState, EnumFacing, Long, util.List[BakedQuad]) => util.List[BakedQuad]
  val NOOP: Processor = (_, _, _, l) => l
  def combine(list: List[Processor]): Processor = {
    if (list.isEmpty) NOOP
    else if (list.length == 1) list.head
    else list.tail.foldLeft(list.head)((p1, p2) => (s, f, r, l) => p1(s, f, r, p2(s, f, r, l)))
  }
}

object NoCameraTransforms extends ((IBakedModel, TransformType) => (IBakedModel, Matrix4f)) {
  lazy val matrix = TRSRTransformation.identity().getMatrix
  override def apply(m: IBakedModel, t: TransformType) = (m, matrix)
}

class SimpleBakedModelBuilder(format: VertexFormat = DefaultVertexFormats.ITEM) {
  val faceQuads = EnumFacing.values().map(f => f -> mutable.ListBuffer.empty[BakedQuad]).toMap
  var generalQuads = mutable.ListBuffer.empty[BakedQuad]
  var texture: TextureAtlasSprite = Client.missingIcon
  var isAmbientOcclusion = true
  var isGui3d = false
  var isBuiltInRenderer = false
  var itemOverrides = ItemOverrideList.NONE
  var cameraTransforms: ((IBakedModel, TransformType) => (IBakedModel, Matrix4f)) = NoCameraTransforms
  var processing = List.empty[ModelProcessing.Processor]

  def compose(f1: (IBlockState, EnumFacing, Long, util.List[BakedQuad]) => util.List[BakedQuad], f2: (IBlockState, EnumFacing, Long, util.List[BakedQuad]) => util.List[BakedQuad]): (IBlockState, EnumFacing, Long, util.List[BakedQuad]) => util.List[BakedQuad] =
    (s: IBlockState, f: EnumFacing, r: Long, l: util.List[BakedQuad]) => f1(s, f, r, f2(s, f, r, l))

  lazy val baker = new QuadBaker(format)

  // Unbaked versions

  def addQuad(face: EnumFacing, quad: TQuad) = faceQuads(face) += baker.bakeQuad(quad)

  def addQuads(face: EnumFacing, quads: List[TQuad]) = faceQuads(face) ++= baker.bakeList(quads)

  def addQuadMap(map: Map[EnumFacing, TQuad]) =
    for ((face, quad) <- map) faceQuads(face) += baker.bakeQuad(quad)

  def addQuadListMap(map: Map[EnumFacing, List[TQuad]]) =
    for ((face, quads) <- map) faceQuads(face) ++= baker.bakeList(quads)

  def addQuadGeneral(quad: TQuad) = generalQuads += baker.bakeQuad(quad)

  def addQuadsGeneral(quads: List[TQuad]) = generalQuads ++= baker.bakeList(quads)

  // Baked versions

  def addBakedQuad(face: EnumFacing, quad: BakedQuad) = faceQuads(face) += quad

  def addBakedQuads(face: EnumFacing, quads: List[BakedQuad]) = faceQuads(face) ++= quads

  def addBakedQuadMap(map: Map[EnumFacing, BakedQuad]) =
    for ((face, quad) <- map) faceQuads(face) += quad

  def addBakedQuadListMap(map: Map[EnumFacing, List[BakedQuad]]) =
    for ((face, quads) <- map) faceQuads(face) ++= quads

  def addBakedQuadGeneral(quad: BakedQuad) = generalQuads += quad

  def addBakedQuadsGeneral(quads: List[BakedQuad]) = generalQuads ++= quads

  def inheritCameraTransformsFrom(model: IBakedModel): Unit = {
    cameraTransforms = { (m, t) => m -> model.handlePerspective(t).getRight }
  }

  def setTransformsFromState(state: IModelState): Unit = {
    cameraTransforms = { (m, t) =>
      val tr = state.apply(util.Optional.of(t)).orElse(TRSRTransformation.identity)
      if (tr != TRSRTransformation.identity)
        (m, TRSRTransformation.blockCornerToCenter(tr).getMatrix)
      else
        (m, null)
    }
  }

  def addProcessing(p: ModelProcessing.Processor) = processing :+= p

  def build(): IBakedModel = {
    if (format == null) throw new RuntimeException("Format not specified")
    SimpleBakedModel(
      generalQuads = Misc.jImmutable(generalQuads),
      faceQuads = faceQuads.mapValues(Misc.jImmutable),
      texture = texture,
      format = format,
      transforms = cameraTransforms,
      processing = ModelProcessing.combine(processing),
      itemOverrideList = itemOverrides,
      isAmbientOcclusion = isAmbientOcclusion,
      isGui3d = isGui3d,
      isBuiltInRenderer = isBuiltInRenderer
    )
  }
}