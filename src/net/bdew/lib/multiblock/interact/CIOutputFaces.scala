/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.block.{BlockFace, BlockRef}
import net.bdew.lib.multiblock.data._
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.util.ForgeDirection

trait CIOutputFaces extends TileController {
  val maxOutputs: Int

  val outputFaces = new DataSlotBlockFaceMap("outputs", this)
  val outputConfig = new DataSlotOutputConfig("cfg", this, maxOutputs)

  def newOutput(bp: BlockRef, face: ForgeDirection, cfg: OutputConfig): Int = {
    val bf = BlockFace(bp, face)
    if (outputFaces.contains(bf)) {
      println("Adding already registered output??? " + bf.toString)
      return outputFaces(bf)
    }
    val rv = outputFaces.inverted
    for (i <- 0 until maxOutputs if !rv.contains(i)) {
      outputFaces += (bf -> i)
      outputConfig += i -> cfg
      outputFaces.updated()
      return i
    }
    val pl = getWorldObj.getClosestPlayer(bf.x, bf.y, bf.z, 10)
    if (pl != null) pl.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.toomanyoutputs"))
    return -1
  }

  serverTick.listen(doOutputs)

  override def moduleRemoved(module: TileModule) {
    for ((bf, n) <- outputFaces if bf.origin == module.mypos) {
      outputFaces -= bf
      outputConfig -= n
    }
    outputFaces.updated()
    outputConfig.updated()
    super.moduleRemoved(module)
  }

  def doOutputs() {
    for ((x, n) <- outputFaces) {
      val t = x.origin.getTile[MIOutput](getWorldObj)
      if (t.isDefined) {
        if (!outputConfig.isDefinedAt(n) || outputConfig(n).isInstanceOf[OutputConfigInvalid])
          outputConfig(n) = t.get.makeCfgObject(x.face)
        t.get.doOutput(x.face, outputConfig(n))
      }
    }
  }

  def removeOutput(bp: BlockRef, face: ForgeDirection) {
    val bf = BlockFace(bp, face)
    outputConfig -= outputFaces(bf)
    outputFaces -= bf
    outputFaces.updated()
  }
}
