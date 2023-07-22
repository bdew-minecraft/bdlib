package net.bdew.lib.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.bdew.lib.misc.Taggable
import net.bdew.lib.{BdLib, Text}
import net.minecraft.commands.CommandSourceStack
import net.minecraft.tags._
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistry}

import java.io.{BufferedWriter, FileWriter}
import scala.jdk.CollectionConverters._
import scala.util.Using

object CommandDumpRegistry extends ModCommand {
  override def register: LiteralArgumentBuilder[CommandSourceStack] = {
    literal("dumpregistry")
      .executes(cs => execute(cs))
  }

  def dumpRegistry[T](dumpWriter: BufferedWriter, registry: IForgeRegistry[T]): Unit = {
    dumpWriter.write(registry.getKeys.asScala.map(_.toString).toList.sorted.mkString("\n"))
  }

  def dumpTag[T](dumpWriter: BufferedWriter, tag: TagKey[T], entries: List[T], reg: IForgeRegistry[T]): Unit = {
    dumpWriter.write(tag.location().toString)
    dumpWriter.newLine()
    dumpWriter.write(entries.map(x => s" - ${reg.getKey(x).toString}").sorted.mkString("\n"))
    dumpWriter.newLine()
  }

  def dumpTags[T](dumpWriter: BufferedWriter, kind: Taggable[T]): Unit = {
    kind.tagMap.toList.sortBy(_._1.toString).foreach(x => dumpTag(dumpWriter, x._1, x._2, kind.registry))
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
      dumpTags(dumpWriter, Taggable[Block])

      dumpWriter.write("\n\n==== ITEM TAGS ====\n")
      dumpTags(dumpWriter, Taggable[Item])

      dumpWriter.write("\n\n==== FLUID TAGS ====\n")
      dumpTags(dumpWriter, Taggable[Fluid])

      ctx.getSource.sendSuccess(() => Text.translate("bdlib.dumpregistry.saved", dumpFile.getCanonicalPath), true)
    } recover {
      case e: Throwable =>
        ctx.getSource.sendFailure(Text.translate("bdlib.dumpregistry.error", e.toString))
        BdLib.logErrorException("Failed to save ore distribution dump", e)
    }

    Command.SINGLE_SUCCESS
  }
}
