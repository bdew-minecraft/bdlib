package net.bdew.lib.multiblock

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

import java.util
import scala.jdk.CollectionConverters._

abstract class MultiblockMachineConfig(spec: ForgeConfigSpec.Builder, types: ModuleTypes) extends ConfigSection {
  def defaultRequired: Map[ModuleType, Int]
  def defaultModules: Map[ModuleType, Int]

  private def toConfig(m: Map[ModuleType, Int]): util.List[String] =
    m.map(x => s"${x._1.id}:${x._2}").toList.asJava

  private def fromConfig(vals: util.List[_ <: String]): Map[ModuleType, Int] =
    vals.asScala.map(x => {
      val sp = x.split(":")
      types.all(sp(0)) -> Integer.parseInt(sp(1), 10)
    }).toMap

  private def validator(v: AnyRef): Boolean = v match {
    case x: String =>
      val sp = x.split(":")
      if (sp.length == 2) {
        try {
          types.all.isDefinedAt(sp(0)) && Integer.parseInt(sp(1), 10) > 0
        } catch {
          case _: NumberFormatException => false
        }
      } else false
    case _ => false
  }

  val required: () => Map[ModuleType, Int] = getter(spec.comment("Required modules for the machine to function")
    .defineList[String]("Required", toConfig(defaultRequired), validator(_)), fromConfig)

  val modules: () => Map[ModuleType, Int] = getter(spec.comment("Modules that can connect to the machine")
    .defineList[String]("Modules", toConfig(defaultModules), validator(_)), fromConfig)
}
