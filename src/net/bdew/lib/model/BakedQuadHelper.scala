/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.model

import com.google.common.base.Optional
import net.minecraft.client.renderer.block.model.{BakedQuad, BlockFaceUV, BlockPartFace, FaceBakery}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.ModelRotation
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.{IModelState, ITransformation}
import org.lwjgl.util.vector.Vector3f

import scala.language.implicitConversions

/**
  * Helper for baking quads
  * @param state as passed to IModel.bake()
  */
class BakedQuadHelper(state: IModelState) {
  private val faceBakery = new FaceBakery
  private val transform = state.apply(Optional.absent()).asInstanceOf[Optional[ITransformation]].or(ModelRotation.X0_Y0)

  def quad(p1: (Float, Float, Float), p2: (Float, Float, Float), side: EnumFacing, texture: TextureAtlasSprite, uv1: (Float, Float), uv2: (Float, Float), rot: Int = 0, uvLocked: Boolean = false, shaded: Boolean = true): BakedQuad = {
    val uv = new BlockFaceUV(Array(uv1._1, uv1._2, uv2._1, uv2._2), rot)
    val face = new BlockPartFace(null, 0, "", uv)
    faceBakery.makeBakedQuad(
      new Vector3f(p1._1, p1._2, p1._3),
      new Vector3f(p2._1, p2._2, p2._3),
      face, texture, side, transform, null, uvLocked, shaded)
  }

  def rotate(f: EnumFacing) = transform.rotate(f)
}

