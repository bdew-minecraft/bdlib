package net.bdew.lib.datagen

import net.bdew.lib.keepdata.{BlockKeepData, KeepDataLootFunction}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.{LootPool, LootTable}

import java.util.function.BiConsumer

object LootTableUtils {
  def addBlockEntry(block: Block, table: LootTable.Builder, consumer: BiConsumer[ResourceLocation, LootTable.Builder]): Unit =
    consumer.accept(block.getLootTable, table.setParamSet(LootContextParamSets.BLOCK))

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
}
