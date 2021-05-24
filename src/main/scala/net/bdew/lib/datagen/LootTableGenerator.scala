package net.bdew.lib.datagen

import com.google.gson.{Gson, GsonBuilder}
import net.minecraft.block.Block
import net.minecraft.data.{DataGenerator, DirectoryCache, IDataProvider}
import net.minecraft.loot.conditions.SurvivesExplosion
import net.minecraft.loot.{LootTable, LootTableManager, _}
import net.minecraft.util.ResourceLocation

abstract class LootTableGenerator(gen: DataGenerator, modId: String) extends IDataProvider {
  val GSON: Gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create

  def makeTables(): Map[ResourceLocation, LootTable]

  def makeBlockEntry(block: Block, table: LootTable.Builder): (ResourceLocation, LootTable) =
    block.getLootTable -> table.setParamSet(LootParameterSets.BLOCK).build()

  def makeSimpleDropTable(block: Block): LootTable.Builder = {
    LootTable.lootTable().withPool(
      LootPool.lootPool()
        .setRolls(ConstantRange.exactly(1))
        .add(ItemLootEntry.lootTableItem(block))
        .when(SurvivesExplosion.survivesExplosion())
    )
  }

  override def getName: String = s"Loot Tables: $modId"

  override def run(cache: DirectoryCache): Unit = {
    val outputFolder = gen.getOutputFolder
    for ((key, lootTable) <- makeTables()) {
      IDataProvider.save(GSON,
        cache,
        LootTableManager.serialize(lootTable),
        outputFolder.resolve("data/" + key.getNamespace + "/loot_tables/" + key.getPath + ".json"))
    }
  }
}
