package net.bdew.lib.keepdata

import com.google.gson.{JsonDeserializationContext, JsonObject}
import net.bdew.lib.BdLib
import net.bdew.lib.registries.LootFunctionTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.functions.{LootItemConditionalFunction, LootItemFunction, LootItemFunctionType}
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition

class KeepDataLootFunction(conditions: Array[LootItemCondition]) extends LootItemConditionalFunction(conditions) {
  override def run(stack: ItemStack, context: LootContext): ItemStack = {
    val te = context.getParamOrNull(LootContextParams.BLOCK_ENTITY)
    te match {
      case x: TileKeepData => x.saveDataToItem(stack)
      case x => BdLib.logWarn(s"keep_data loot function called on invalid TE $x")
    }
    stack
  }

  override def getType: LootItemFunctionType = LootFunctionTypes.keepData.get
}

class KeepDataLootFunctionSerializer extends LootItemConditionalFunction.Serializer[KeepDataLootFunction] {
  override def deserialize(obj: JsonObject, ctx: JsonDeserializationContext, conditions: Array[LootItemCondition]): KeepDataLootFunction =
    new KeepDataLootFunction(conditions)
}

object KeepDataLootFunction {
  class Builder extends LootItemConditionalFunction.Builder[Builder] {
    override def getThis: Builder = this
    override def build(): LootItemFunction = new KeepDataLootFunction(getConditions)
  }

  def keepData = new Builder
}