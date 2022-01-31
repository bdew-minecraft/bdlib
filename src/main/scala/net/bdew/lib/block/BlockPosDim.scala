package net.bdew.lib.block

import net.bdew.lib.nbt.Type.TInt
import net.bdew.lib.nbt.{ConvertedType, NBT}
import net.minecraft.core.{BlockPos, Registry}
import net.minecraft.nbt.{CompoundTag, Tag}
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level

case class BlockPosDim(pos: BlockPos, dim: ResourceKey[Level]) {
  def x: Int = pos.getX
  def y: Int = pos.getY
  def z: Int = pos.getZ
  def world(server: MinecraftServer): Option[ServerLevel] = Option(server.getLevel(dim))
}

object BlockPosDim {
  implicit object TBlockPosDim extends ConvertedType[BlockPosDim, CompoundTag] {
    override def encode(v: BlockPosDim): CompoundTag =
      NBT(
        "x" -> v.pos.getX,
        "y" -> v.pos.getY,
        "z" -> v.pos.getZ,
        "d" -> v.dim.location.toString
      )

    override def decode(t: CompoundTag): Option[BlockPosDim] =
      if (t.contains("x", Tag.TAG_INT) && t.contains("y", Tag.TAG_INT) && t.contains("z", Tag.TAG_INT) && t.contains("d", Tag.TAG_STRING))
        Some(BlockPosDim(
          new BlockPos(t.getInt("x"), t.getInt("y"), t.getInt("z")),
          ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(t.getString("d")))
        ))
      else
        None
  }
}
