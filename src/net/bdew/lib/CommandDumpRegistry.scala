/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.io.{BufferedWriter, File, FileWriter}

import net.minecraft.block.Block
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.item.Item
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.relauncher.FMLInjectionData
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistryEntry

object CommandDumpRegistry extends CommandBase {
  override def getName = "dumpregistry"
  override def getRequiredPermissionLevel = 2
  override def getUsage(c: ICommandSender) = "dumpregistry"

  implicit object ResourceLocationOrdering extends Ordering[ResourceLocation] {
    override def compare(x: ResourceLocation, y: ResourceLocation) =
      if (x.getResourceDomain == y.getResourceDomain)
        x.getResourcePath.compareTo(x.getResourcePath)
      else
        x.getResourceDomain.compareTo(x.getResourceDomain)
  }

  def sanitize(x: IForgeRegistryEntry[_]): Option[String] = {
    Option(x.getRegistryName) orElse {
      x match {
        case item: Item =>
          BdLib.logWarn("Item with null name in registry! Key=%s Unlocalized=%s Class=%s",
            Item.REGISTRY.getNameForObject(item), item.getUnlocalizedName, x.getClass.getName)
        case block: Block =>
          BdLib.logWarn("Block with null name in registry! Key=%s Unlocalized=%s Class=%s",
            Block.REGISTRY.getNameForObject(block), block.getUnlocalizedName, x.getClass.getName)
        case _ => BdLib.logWarn("Entry with null name in registry! Class=%s", x.getClass.getName)
      }
      None
    } map (_.toString)
  }

  override def execute(server: MinecraftServer, sender: ICommandSender, params: Array[String]): Unit = {
    val mcHome = FMLInjectionData.data()(6).asInstanceOf[File] //is there a better way to get this?
    val dumpFile = new File(mcHome, "registry.dump")
    val dumpWriter = new BufferedWriter(new FileWriter(dumpFile))
    import scala.collection.JavaConversions._
    try {
      dumpWriter.write("==== BLOCKS ====\n")
      dumpWriter.write(Block.REGISTRY.flatMap(sanitize).toList.sorted.mkString("\n"))
      dumpWriter.write("\n\n")

      dumpWriter.write("==== ITEMS ====\n")
      dumpWriter.write(Item.REGISTRY.flatMap(sanitize).toList.sorted.mkString("\n"))
      dumpWriter.write("\n\n")

      dumpWriter.write("==== ORE DICT ====\n")
      dumpWriter.write((OreDictionary.getOreNames.sorted flatMap { name =>
        List(name) ++
          (OreDictionary.getOres(name) map { ore =>
            " - " + ore.toString
          })
      }).mkString("\n"))
      dumpWriter.write("\n\n")

      dumpWriter.write("==== FLUIDS ====\n")
      dumpWriter.write(FluidRegistry.getRegisteredFluids.map(_._1).toList.sorted.mkString("\n"))
      dumpWriter.write("\n\n")

      CommandBase.notifyCommandListener(sender, this, "Registry dumped to " + dumpFile.getCanonicalPath)
    } catch {
      case e: Throwable =>
        CommandBase.notifyCommandListener(sender, this, "Failed to save registry dump: " + e)
        BdLib.logErrorException("Failed to save registry dump", e)
    } finally {
      dumpWriter.close()
    }
  }
}