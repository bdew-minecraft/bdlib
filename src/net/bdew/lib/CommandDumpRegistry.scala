/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.io.{BufferedWriter, File, FileWriter}

import com.google.common.collect.Table
import cpw.mods.fml.common.registry.GameData
import cpw.mods.fml.relauncher.FMLInjectionData
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary

object CommandDumpRegistry extends CommandBase {
  def getCommandName = "dumpregistry"
  override def getRequiredPermissionLevel = 2
  def getCommandUsage(c: ICommandSender) = "dumpregistry"

  def processCommand(sender: ICommandSender, params: Array[String]) {
    val mcHome = FMLInjectionData.data()(6).asInstanceOf[File] //is there a better way to get this?
    val dumpFile = new File(mcHome, "registry.dump")
    val dumpWriter = new BufferedWriter(new FileWriter(dumpFile))
    import scala.collection.JavaConversions._
    try {
      dumpWriter.write("==== BLOCKS ====\n")
      dumpWriter.write(GameData.getBlockRegistry.map(GameData.getBlockRegistry.getNameForObject).toList.sorted.mkString("\n"))
      dumpWriter.write("\n\n")

      dumpWriter.write("==== ITEMS ====\n")
      dumpWriter.write(GameData.getItemRegistry.map(GameData.getItemRegistry.getNameForObject).toList.sorted.mkString("\n"))
      dumpWriter.write("\n\n")

      dumpWriter.write("==== CUSTOM STACKS ====\n")
      val stacksField = classOf[GameData].getDeclaredField("customItemStacks")
      stacksField.setAccessible(true)
      val stacksData = stacksField.get(null).asInstanceOf[Table[String, String, ItemStack]]
      dumpWriter.write(stacksData.cellSet().map(x => x.getRowKey + ":" + x.getColumnKey).toList.sorted.mkString("\n"))
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

      CommandBase.func_152373_a(sender, this, "Registry dumped to " + dumpFile.getCanonicalPath)
    } catch {
      case e: Throwable =>
        CommandBase.func_152373_a(sender, this, "Failed to save registry dump: " + e)
        BdLib.logErrorException("Failed to save registry dump", e)
    } finally {
      dumpWriter.close()
    }
  }
}