package net.bdew.lib.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.bdew.lib.{BdLib, Text}
import net.minecraft.commands.CommandSourceStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags._
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.registries.{ForgeRegistries, ForgeRegistryEntry, IForgeRegistry}

import java.io.{BufferedWriter, FileWriter}
import scala.jdk.CollectionConverters._
import scala.util.Using

object CommandDumpRegistry extends ModCommand {
  override def register: LiteralArgumentBuilder[CommandSourceStack] = {
    literal("dumpregistry")
      .executes(cs => execute(cs))
  }
  def dumpRegistry[T <: ForgeRegistryEntry[T]](dumpWriter: BufferedWriter, registry: IForgeRegistry[T]): Unit = {
    dumpWriter.write(registry.getKeys.asScala.map(_.toString).toList.sorted.mkString("\n"))
  }

  def dumpTag[T <: ForgeRegistryEntry[T]](dumpWriter: BufferedWriter, loc: ResourceLocation, tag: Tag[T]): Unit = {
    dumpWriter.write(loc.toString)
    dumpWriter.newLine()
    dumpWriter.write(tag.getValues.asScala.map(x => s" - ${x.getRegistryName.toString}").sorted.mkString("\n"))
    dumpWriter.newLine()
  }

  def dumpTags[T <: ForgeRegistryEntry[T]](dumpWriter: BufferedWriter, tags: TagCollection[T]): Unit = {
    tags.getAllTags.asScala.toList.sortBy(_._1.toString).foreach(x => dumpTag(dumpWriter, x._1, x._2))
  }

  def execute(ctx: CommandContext[CommandSourceStack]): Int = {
    val dumpFile = FMLPaths.GAMEDIR.get().resolve("registry.dump").toFile

    Using(new BufferedWriter(new FileWriter(dumpFile))) { dumpWriter =>
      dumpWriter.write("==== BLOCKS ====\n")
      dumpRegistry(dumpWriter, ForgeRegistries.BLOCKS)

      dumpWriter.write("\n\n==== ITEMS ====\n")
      dumpRegistry(dumpWriter, ForgeRegistries.ITEMS)

      dumpWriter.write("\n\n==== FLUIDS ====\n")
      dumpRegistry(dumpWriter, ForgeRegistries.FLUIDS)

      dumpWriter.write("\n\n==== BLOCK TAGS ====\n")
      dumpTags(dumpWriter, BlockTags.getAllTags)

      dumpWriter.write("\n\n==== ITEM TAGS ====\n")
      dumpTags(dumpWriter, ItemTags.getAllTags)

      dumpWriter.write("\n\n==== FLUID TAGS ====\n")
      dumpTags(dumpWriter, FluidTags.getAllTags)

      ctx.getSource.sendSuccess(Text.translate("bdlib.dumpregistry.saved", dumpFile.getCanonicalPath), true)
    } recover {
      case e: Throwable =>
        ctx.getSource.sendFailure(Text.translate("bdlib.dumpregistry.error", e.toString))
        BdLib.logErrorException("Failed to save ore distribution dump", e)
    }

    Command.SINGLE_SUCCESS
  }
}
