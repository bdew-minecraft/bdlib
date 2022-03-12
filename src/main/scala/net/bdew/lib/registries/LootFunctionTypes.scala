package net.bdew.lib.registries

import net.bdew.lib.keepdata.KeepDataLootFunctionSerializer
import net.bdew.lib.managers.{VanillaRegistryManager, VanillaRegistryObject}
import net.minecraft.core.Registry
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

object LootFunctionTypes extends VanillaRegistryManager(Registry.LOOT_FUNCTION_TYPE) {
  val keepData: VanillaRegistryObject[LootItemFunctionType] =
    register("keep_data", () => new LootItemFunctionType(new KeepDataLootFunctionSerializer))
}
