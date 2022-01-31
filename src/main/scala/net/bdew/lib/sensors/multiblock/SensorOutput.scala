package net.bdew.lib.sensors.multiblock

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui._
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.sensors.{GenericSensorParameter, GenericSensorType, SensorSystem}
import net.bdew.lib.{Misc, Text}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

import java.util.Locale

trait SensorOutput extends GenericSensorType[BlockEntity, Boolean] {
  def system: SensorSystem[BlockEntity, Boolean]

  override def defaultParameter: GenericSensorParameter = system.DisabledParameter

  case class SensorOutputFlowParameter(output: Int) extends GenericSensorParameter(system) {
    override val uid: String = "output." + output
  }

  def getResultFromOutput(te: CIOutputFaces, output: Int): Boolean

  override def loadParameter(tag: CompoundTag): GenericSensorParameter = {
    if (tag.contains("output"))
      SensorOutputFlowParameter(tag.getInt("output"))
    else
      system.DisabledParameter
  }

  override def saveParameter(p: GenericSensorParameter, tag: CompoundTag): Unit = p match {
    case SensorOutputFlowParameter(n) => tag.putInt("output", n)
    case _ =>
  }

  override def isValidParameter(p: GenericSensorParameter, obj: BlockEntity): Boolean = (p, obj) match {
    case (SensorOutputFlowParameter(n), x: CIOutputFaces) => x.outputFaces.inverted.isDefinedAt(n)
    case (param, _) if param == system.DisabledParameter => true
    case _ => false
  }

  override def getResult(param: GenericSensorParameter, obj: BlockEntity): Boolean = (param, obj) match {
    case (SensorOutputFlowParameter(n), x: CIOutputFaces) => getResultFromOutput(x, n)
    case _ => false
  }

  override def paramClicked(current: GenericSensorParameter, item: ItemStack, clickType: ClickType, button: Int, obj: BlockEntity): GenericSensorParameter =
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
  override def drawParameter(m: PoseStack, rect: Rect, target: DrawTarget, obj: BlockEntity, param: GenericSensorParameter): Unit = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      if (faces.isDefinedAt(output)) {
        val bf = faces(output)
        // fixme
        //        ModelDrawHelper.renderWorldBlockIntoGUI(te.getWorld, bf.pos, bf.face, rect)
      }

    case _ => target.drawTexture(m, rect, system.DisabledParameter.texture, system.DisabledParameter.textureColor)
  }

  override def getParamTooltip(obj: BlockEntity, param: GenericSensorParameter): List[Component] = (param, obj) match {
    case (SensorOutputFlowParameter(output), te: CIOutputFaces) =>
      val faces = te.outputFaces.inverted
      var list = List.empty[Component]
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
