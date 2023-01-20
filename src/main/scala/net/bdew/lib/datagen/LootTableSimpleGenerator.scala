package net.bdew.lib.datagen

import net.minecraft.data.PackOutput
import net.minecraft.data.loot.{LootTableProvider, LootTableSubProvider}
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet

class LootTableSimpleGenerator(out: PackOutput, context: LootContextParamSet, factory: () => LootTableSubProvider) extends
  LootTableProvider(out, java.util.Set.of(),
    java.util.List.of(new LootTableProvider.SubProviderEntry(() => factory(), context)))
