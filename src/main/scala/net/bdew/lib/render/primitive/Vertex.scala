package net.bdew.lib.render.primitive

import com.mojang.math.Transformation
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import org.joml.Vector4f

/**
 * Vertex coordinates
 */
case class Vertex(x: Float, y: Float, z: Float) {
  /**
   * Combines vertex data with texture data
   *
   * @param sprite vanilla texture sprite
   * @param uv     coordinates in sprite (0-16)
   * @return combined data, UV translated to sprite sheet coordinates
   */
  def withTexture(sprite: TextureAtlasSprite, uv: UV): TVertex =
    TVertex(x, y, z, sprite.getU(uv.u), sprite.getV(uv.v))

  /**
   * Applies a transformation to the vertex data
   */
  def applyTransformation(t: Transformation): Vertex = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.transformPosition(tmp)
    Vertex(tmp.x / tmp.w, tmp.y / tmp.w, tmp.z / tmp.w)
  }

  def transform(transformation: Float => Float): Vertex = {
    Vertex(transformation(x), transformation(y), transformation(z))
  }
}

/**
 * Vertex + Texture coordinates
 */
case class TVertex(x: Float, y: Float, z: Float, u: Float, v: Float) {
  /**
   * Applies a transformation to the vertex data, leaving texture untouched
   */
  def applyTransformation(t: Transformation): TVertex = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.transformPosition(tmp)
    copy(x = tmp.x / tmp.w, y = tmp.y / tmp.w, z = tmp.z / tmp.w)
  }
}