/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
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
    for ((bf, n) <- outputFaces if bf.origin == module.myPos) {
      outputFaces -= bf
      outputConfig -= n
    }
    outputFaces.updated()
    outputConfig.updated()
    super.moduleRemoved(module)
  }

  def doOutputs() {
    for {
      (x, n) <- outputFaces
      t <- x.origin.getTile[MIOutput[_]](getWorldObj)
    } {
      if (!outputConfig.isDefinedAt(n) || !t.outputConfigType.isInstance(outputConfig(n)))
        outputConfig(n) = t.makeCfgObject(x.face)
      t.doOutput(x.face, outputConfig(n).asInstanceOf[t.OCType])
    }
  }

  def removeOutput(bp: BlockRef, face: ForgeDirection) {
    val bf = BlockFace(bp, face)
    outputConfig -= outputFaces(bf)
    outputFaces -= bf
    outputFaces.updated()
  }
}
