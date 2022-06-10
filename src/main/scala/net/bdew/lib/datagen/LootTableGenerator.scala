package net.bdew.lib.datagen

import net.bdew.lib.keepdata.{BlockKeepData, KeepDataLootFunction}
import net.minecraft.data.{CachedOutput, DataGenerator, DataProvider}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.{LootPool, LootTable, LootTables}

abstract class LootTableGenerator(gen: DataGenerator, modId: String) extends DataProvider {
  def makeTables(): Map[ResourceLocation, LootTable]

  def makeBlockEntry(block: Block, table: LootTable.Builder): (ResourceLocation, LootTable) =
    block.getLootTable -> table.setParamSet(LootContextParamSets.BLOCK).build()

  def makeSimpleDropTable(block: Block): LootTable.Builder = {
    LootTable.lootTable().withPool(
      LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block))
        .when(ExplosionCondition.survivesExplosion())
    )
  }

  def makeKeepDataDropTable(block: BlockKeepData): LootTable.Builder = {
    LootTable.lootTable().withPool(
      LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block).apply(KeepDataLootFunction.keepData))
        .when(ExplosionCondition.survivesExplosion())
    )
  }

  override def getName: String = s"Loot Tables: $modId"

  override def run(cache: CachedOutput): Unit = {
    val outputFolder = gen.getOutputFolder
    for ((key, lootTable) <- makeTables()) {
      DataProvider.saveStable(
        cache,
        LootTables.serialize(lootTable),
        outputFolder.resolve("data/" + key.getNamespace + "/loot_tables/" + key.getPath + ".json"))
    }
  }
}
