/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockFace
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.data._
import net.bdew.lib.multiblock.tile.{TileController, TileModule, TileOutput}
import net.bdew.lib.{BdLib, Client, Text}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{Direction, Util}

trait CIOutputFaces extends TileController {
  val maxOutputs: Int

  val outputFaces: DataSlotBlockFaceMap = new DataSlotBlockFaceMap("outputs", this) {
    override def load(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
      super.load(t, kind)
      if (kind == UpdateKind.WORLD) {
        getModuleTiles[TileOutput[_]].foreach(t => {
          t.requestModelDataUpdate()
          Client.doRenderUpdate(t.getBlockPos)
        })
      }
    }
  }

  val outputConfig: DataSlotOutputConfig = new DataSlotOutputConfig("cfg", this, maxOutputs)

  def newOutput(bp: BlockPos, face: Direction, cfg: OutputConfig): Int = {
    val bf = BlockFace(bp, face)
    if (outputFaces.contains(bf)) {
      BdLib.logWarn("Adding already registered output??? " + bf.toString)
      return outputFaces(bf)
    }
    val rv = outputFaces.inverted
    for (i <- 0 until maxOutputs if !rv.contains(i)) {
      outputFaces += (bf -> i)
      outputConfig += i -> cfg
      outputFaces.updated()
      return i
    }
    val pl = getLevel.getNearestPlayer(bf.x, bf.y, bf.z, 10, false)
    if (pl != null) pl.sendMessage(Text.translate("bdlib.multiblock.toomanyoutputs"), Util.NIL_UUID)
    -1
  }

  serverTick.listen(doOutputs)

  override def moduleRemoved(module: TileModule): Unit = {
    for ((bf, n) <- outputFaces if bf.pos == module.getBlockPos) {
      outputFaces -= bf
      outputConfig -= n
    }
    outputFaces.updated()
    outputConfig.updated()
    super.moduleRemoved(module)
  }

  def doOutputs(): Unit = {
    for {
      (x, n) <- outputFaces
      t <- getLevel.getTileSafe[MIOutput[_]](x.pos)
    } {
      if (!outputConfig.isDefinedAt(n) || !t.outputConfigType.isInstance(outputConfig(n)))
        outputConfig(n) = t.makeCfgObject(x.face)
      t.doOutput(x.face, outputConfig(n).asInstanceOf[t.OCType])
    }
  }

  def removeOutput(bp: BlockPos, face: Direction): Unit = {
    val bf = BlockFace(bp, face)
    outputConfig -= outputFaces(bf)
    outputFaces -= bf
    outputFaces.updated()
  }
}
