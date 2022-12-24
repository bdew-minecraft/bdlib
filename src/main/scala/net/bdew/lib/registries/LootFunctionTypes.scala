package net.bdew.lib.registries

import net.bdew.lib.keepdata.KeepDataLootFunctionSerializer
import net.bdew.lib.managers.VanillaRegistryManager
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
import net.minecraftforge.registries.RegistryObject

object LootFunctionTypes extends VanillaRegistryManager(BuiltInRegistries.LOOT_FUNCTION_TYPE) {
  val keepData: RegistryObject[LootItemFunctionType] =
    register("keep_data", () => new LootItemFunctionType(new KeepDataLootFunctionSerializer))
}
