/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import java.util.Locale

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.render.connected.BlockAdditionalRender
import net.bdew.lib.sensors.{GenericSensorParameter, GenericSensorType, SensorSystem}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

trait SensorOutput extends GenericSensorType[TileEntity, Boolean] {
  def system: SensorSystem[TileEntity, Boolean]

  override def defaultParameter = system.DisabledParameter

  case class SensorOutputFlowParameter(output: Int) extends GenericSensorParameter(system) {
    override val uid: String = "output." + output
  }

  def getResultFromOutput(te: CIOutputFaces, output: Int): Boolean

  override def loadParameter(tag: NBTTagCompound) = {
    if (tag.hasKey("output"))
      SensorOutputFlowParameter(tag.getInteger("output"))
    else
      system.DisabledParameter
  }

  override def saveParameter(p: GenericSensorParameter, tag: NBTTagCompound): Unit = p match {
    case SensorOutputFlowParameter(n) => tag.setInteger("output", n)
    case _ =>
  }

  override def isValidParameter(p: GenericSensorParameter, obj: TileEntity): Boolean = (p, obj) match {
    case (SensorOutputFlowParameter(n), x: CIOutputFaces) => x.outputFaces.inverted.isDefinedAt(n)
    case (param, _) if param == system.DisabledParameter => true
    case _ => false
  }

  override def getResult(param: GenericSensorParameter, obj: TileEntity): Boolean = (param, obj) match {
    case (SensorOutputFlowParameter(n), x: CIOutputFaces) => getResultFromOutput(x, n)
    case _ => false
  }

  override def paramClicked(current: GenericSensorParameter, item: ItemStack, button: Int, mod: Int, obj: TileEntity) =
    if (mod == 0 && (button == 0 || button == 1) && item == null)
      (current, obj) match {
        case (SensorOutputFlowParameter(n), x: CIOutputFaces) =>
          val outputs = x.outputFaces.map.values.toList.sorted
          if (outputs.nonEmpty)
            if (button == 0)
              SensorOutputFlowParameter(Misc.nextInSeq(outputs, n))
            else
              SensorOutputFlowParameter(Misc.prevInSeq(outputs, n))
          else
            system.DisabledParameter
        case (_, x: CIOutputFaces) =>
          if (button == 0)
            x.outputFaces.map.values.toList.sorted.headOption map SensorOutputFlowParameter getOrElse system.DisabledParameter
          else
            x.outputFaces.map.values.toList.sorted.reverse.headOption map SensorOutputFlowParameter getOrElse system.DisabledParameter
        case _ =>
          system.DisabledParameter
      }
    else current

  @SideOnly(Side.CLIENT)
  override def drawParameter(rect: Rect, target: DrawTarget, obj: TileEntity, param: GenericSensorParameter): Unit = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      if (faces.isDefinedAt(output)) {
        val bf = faces(output)
        bf.origin.block(te.getWorldObj) foreach { block =>
          target.drawTexture(rect, Texture(Texture.BLOCKS, block.getIcon(te.getWorldObj, bf.origin.x, bf.origin.y, bf.origin.z, bf.face.ordinal())))
          if (block.isInstanceOf[BlockAdditionalRender]) {
            for (over <- block.asInstanceOf[BlockAdditionalRender].getFaceOverlays(te.getWorldObj, bf.origin.x, bf.origin.y, bf.origin.z, bf.face))
              target.drawTexture(rect, Texture(Texture.BLOCKS, over.icon), over.color)
          }
        }
      }

    case _ => target.drawTexture(rect, system.DisabledParameter.texture, system.DisabledParameter.textureColor)
  }

  override def getParamTooltip(obj: TileEntity, param: GenericSensorParameter) = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      var list = List.empty[String]
      list :+= Misc.toLocal(te.resources.unlocalizedOutputName(output))
      if (faces.isDefinedAt(output)) {
        val bf = faces(output)
        bf.origin.block(te.getWorldObj) foreach { block =>
          list :+= block.getLocalizedName
          list :+= "%d, %d, %d - %s".format(bf.x, bf.y, bf.z, Misc.toLocal("bdlib.multiblock.face." + bf.face.toString.toLowerCase(Locale.US)))
        }
      }
      list
    case _ => super.getParamTooltip(obj, param)
  }
}
