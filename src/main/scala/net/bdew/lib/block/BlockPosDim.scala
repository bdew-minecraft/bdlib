package net.bdew.lib.block

import net.bdew.lib.nbt.Type.TInt
import net.bdew.lib.nbt.{ConvertedType, NBT}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.{RegistryKey, ResourceLocation}
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.util.Constants

case class BlockPosDim(pos: BlockPos, dim: RegistryKey[World]) {
  def x: Int = pos.getX
  def y: Int = pos.getY
  def z: Int = pos.getZ
  def world(server: MinecraftServer): Option[ServerWorld] = Option(server.getLevel(dim))
}

object BlockPosDim {
  implicit object TBlockPosDim extends ConvertedType[BlockPosDim, CompoundNBT] {
    override def encode(v: BlockPosDim): CompoundNBT =
      NBT(
        "x" -> v.pos.getX,
        "y" -> v.pos.getY,
        "z" -> v.pos.getZ,
        "d" -> v.dim.location.toString
      )

    override def decode(t: CompoundNBT): Option[BlockPosDim] =
      if (t.contains("x", Constants.NBT.TAG_INT) && t.contains("y", Constants.NBT.TAG_INT) && t.contains("z", Constants.NBT.TAG_INT) && t.contains("d", Constants.NBT.TAG_STRING))
        Some(BlockPosDim(
          new BlockPos(t.getInt("x"), t.getInt("y"), t.getInt("z")),
          RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(t.getString("d")))
        ))
      else
        None
  }
}
