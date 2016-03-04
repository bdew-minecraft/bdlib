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
import javax.vecmath.Matrix4f

import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import net.bdew.lib.Client
import net.bdew.lib.render.QuadBaker
import net.bdew.lib.render.primitive.TQuad
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemCameraTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model._
import org.apache.commons.lang3.tuple.Pair

import scala.collection.mutable

case class SimpleBakedModel(generalQuads: util.List[BakedQuad],
                            faceQuads: Map[EnumFacing, util.List[BakedQuad]],
                            texture: TextureAtlasSprite,
                            format: VertexFormat,
                            transforms: (IFlexibleBakedModel, TransformType) => (IFlexibleBakedModel, Matrix4f),
                            isAmbientOcclusion: Boolean,
                            isGui3d: Boolean,
                            isBuiltInRenderer: Boolean
                           ) extends IPerspectiveAwareModel {
  override def getFormat = format
  override def getParticleTexture = texture
  override def getGeneralQuads = generalQuads
  override def getFaceQuads(face: EnumFacing) = faceQuads(face)
  override def getItemCameraTransforms = ItemCameraTransforms.DEFAULT
  override def handlePerspective(cameraTransformType: TransformType) = {
    val (model, matrix) = transforms(this, cameraTransformType)
    Pair.of(model, matrix)
  }
}

object NoCameraTransforms extends ((IFlexibleBakedModel, TransformType) => (IFlexibleBakedModel, Matrix4f)) {
  lazy val matrix = TRSRTransformation.identity().getMatrix
  override def apply(m: IFlexibleBakedModel, t: TransformType) = (m, matrix)
}

class SimpleBakedModelBuilder(format: VertexFormat) {
  val faceQuads = EnumFacing.values().map(f => f -> mutable.ListBuffer.empty[BakedQuad]).toMap
  var generalQuads = mutable.ListBuffer.empty[BakedQuad]
  var texture: TextureAtlasSprite = Client.missingIcon
  var isAmbientOcclusion = true
  var isGui3d = false
  var isBuiltInRenderer = false
  var cameraTransforms: ((IFlexibleBakedModel, TransformType) => (IFlexibleBakedModel, Matrix4f)) = NoCameraTransforms

  lazy val baker = new QuadBaker(format)

  def addQuad(face: EnumFacing, quad: TQuad) = faceQuads(face) += baker.bakeQuad(quad)

  def addQuads(face: EnumFacing, quads: List[TQuad]) = faceQuads(face) ++= baker.bakeList(quads)

  def addQuadMap(map: Map[EnumFacing, TQuad]) =
    for ((face, quad) <- map) faceQuads(face) += baker.bakeQuad(quad)

  def addQuadListMap(map: Map[EnumFacing, List[TQuad]]) =
    for ((face, quads) <- map) faceQuads(face) ++= baker.bakeList(quads)

  def addQuadGeneral(quad: TQuad) = generalQuads += baker.bakeQuad(quad)

  def addQuadsGeneral(quads: List[TQuad]) = generalQuads ++= baker.bakeList(quads)

  def inheritCameraTransformsFrom(model: IPerspectiveAwareModel): Unit = {
    cameraTransforms = { (m, t) => m -> model.handlePerspective(t).getRight }
  }

  def setTransformsFromState(state: IModelState): Unit = {
    cameraTransforms = { (m, t) =>
      val tr = state.apply(Optional.of(t)).or(TRSRTransformation.identity)
      if (tr != TRSRTransformation.identity)
        (m, TRSRTransformation.blockCornerToCenter(tr).getMatrix)
      else
        (m, null)
    }
  }

  def build(): IPerspectiveAwareModel = {
    import scala.collection.JavaConversions._
    if (format == null) throw new RuntimeException("Format not specified")
    new SimpleBakedModel(
      generalQuads = ImmutableList.copyOf(generalQuads.toIterable),
      faceQuads = faceQuads.mapValues(x => ImmutableList.copyOf(x.toIterable)),
      texture = texture,
      format = format,
      transforms = cameraTransforms,
      isAmbientOcclusion = isAmbientOcclusion,
      isGui3d = isGui3d,
      isBuiltInRenderer = isBuiltInRenderer
    )
  }
}

class SmartBakedModelBuilder(format: VertexFormat) extends SimpleBakedModelBuilder(format) {
  var logic = List.empty[(IPerspectiveAwareModel, IBlockState) => IPerspectiveAwareModel]

  def addLogic(f: (IPerspectiveAwareModel, IBlockState) => IPerspectiveAwareModel) = logic :+= f

  override def build() = {
    val base = super.build()
    new BakedModelProxy(base) with ISmartBlockModel {
      override def handleBlockState(state: IBlockState) =
        logic.foldLeft((base, state))((s, f) => f.tupled(s) -> s._2)._1
    }
  }
}