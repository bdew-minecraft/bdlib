/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui._
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.sensors.{GenericSensorParameter, GenericSensorType, SensorSystem}
import net.bdew.lib.{Misc, Text}
import net.minecraft.inventory.container.ClickType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

import java.util.Locale

trait SensorOutput extends GenericSensorType[TileEntity, Boolean] {
  def system: SensorSystem[TileEntity, Boolean]

  override def defaultParameter: GenericSensorParameter = system.DisabledParameter

  case class SensorOutputFlowParameter(output: Int) extends GenericSensorParameter(system) {
    override val uid: String = "output." + output
  }

  def getResultFromOutput(te: CIOutputFaces, output: Int): Boolean

  override def loadParameter(tag: CompoundNBT): GenericSensorParameter = {
    if (tag.contains("output"))
      SensorOutputFlowParameter(tag.getInt("output"))
    else
      system.DisabledParameter
  }

  override def saveParameter(p: GenericSensorParameter, tag: CompoundNBT): Unit = p match {
    case SensorOutputFlowParameter(n) => tag.putInt("output", n)
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

  override def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: TileEntity): GenericSensorParameter =
    if (clickType == ClickType.PICKUP && (button == 0 || button == 1) && item.isEmpty)
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
    else
      current

  @OnlyIn(Dist.CLIENT)
  override def drawParameter(m: MatrixStack, rect: Rect, target: DrawTarget, obj: TileEntity, param: GenericSensorParameter): Unit = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      if (faces.isDefinedAt(output)) {
        val bf = faces(output)
        // fixme
        //        ModelDrawHelper.renderWorldBlockIntoGUI(te.getWorld, bf.pos, bf.face, rect)
      }

    case _ => target.drawTexture(m, rect, system.DisabledParameter.texture, system.DisabledParameter.textureColor)
  }

  override def getParamTooltip(obj: TileEntity, param: GenericSensorParameter): List[ITextComponent] = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      var list = List.empty[ITextComponent]
      list :+= Text.translate(te.resources.unlocalizedOutputName(output))
      if (faces.isDefinedAt(output)) {
        val bf = faces(output)
        list :+= te.getLevel.getBlockState(bf.pos).getBlock.getName
        list :+= Text.translate("bdlib.multiblock.tip.output.pos", bf.x.toString, bf.y.toString, bf.z.toString,
          Text.translate("bdlib.multiblock.face." + bf.face.toString.toLowerCase(Locale.US)))
      }
      list
    case _ => super.getParamTooltip(obj, param)
  }
}
