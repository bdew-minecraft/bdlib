package net.bdew.lib.render

import net.bdew.lib.Misc
import net.bdew.lib.render.primitive.{TQuad, TVertex}
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraftforge.client.ForgeHooksClient

class QuadBaker {
  def bakeList(quads: List[TQuad]): List[BakedQuad] = quads map bakeQuad

  def bakeMap[T](quads: Map[T, TQuad]): Map[T, BakedQuad] =
    Misc.transformMapValues(quads, bakeQuad)

  def bakeListMap[T](quads: Map[T, List[TQuad]]): Map[T, List[BakedQuad]] =
    Misc.transformMapValues(quads, bakeList)

  def bakeQuad(quad: TQuad): BakedQuad = {
    val data = new Array[Int](32)
    fillRawData(data, 0 * 8, quad.vertexes.v1)
    fillRawData(data, 1 * 8, quad.vertexes.v2)
    fillRawData(data, 2 * 8, quad.vertexes.v3)
    fillRawData(data, 3 * 8, quad.vertexes.v4)
    ForgeHooksClient.fillNormal(data, quad.face)
    new BakedQuad(data, quad.tint, quad.face, quad.sprite, quad.shading)
  }

  private def fillRawData(data: Array[Int], start: Int, vertex: TVertex): Unit = {
    data(start + 0) = java.lang.Float.floatToRawIntBits(vertex.x)
    data(start + 1) = java.lang.Float.floatToRawIntBits(vertex.y)
    data(start + 2) = java.lang.Float.floatToRawIntBits(vertex.z)
    data(start + 3) = -1
    data(start + 4) = java.lang.Float.floatToRawIntBits(vertex.u)
    data(start + 5) = java.lang.Float.floatToRawIntBits(vertex.v)
    // 6 is ???
    // 7 is normal filled by ForgeHooksClient.fillNormal later
  }
}

object QuadBakerDefault extends QuadBaker