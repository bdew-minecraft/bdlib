package net.bdew.lib.keepdata

import com.google.gson.{JsonDeserializationContext, JsonObject}
import net.bdew.lib.BdLib
import net.minecraft.item.ItemStack
import net.minecraft.loot.conditions.ILootCondition
import net.minecraft.loot.{LootContext, LootFunction, LootFunctionType, LootParameters}
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry

class KeepDataLootFunction(conditions: Array[ILootCondition]) extends LootFunction(conditions) {
  override def run(stack: ItemStack, context: LootContext): ItemStack = {
    val te = context.getParamOrNull(LootParameters.BLOCK_ENTITY)
    te match {
      case x: TileKeepData => x.saveToItem(stack)
      case x => BdLib.logWarn(s"keep_data loot function called on invalid TE $x")
    }
    stack
  }

  override def getType: LootFunctionType = KeepDataLootFunction.lfType
}

class KeepDataLootFunctionSerializer extends LootFunction.Serializer[KeepDataLootFunction] {
  override def deserialize(obj: JsonObject, ctx: JsonDeserializationContext, conditions: Array[ILootCondition]): KeepDataLootFunction =
    new KeepDataLootFunction(conditions)
}

object KeepDataLootFunction {
  var lfType: LootFunctionType = _

  def register(): Unit = {
    lfType = Registry.register(Registry.LOOT_FUNCTION_TYPE,
      new ResourceLocation(BdLib.ModId, "keep_data"),
      new LootFunctionType(new KeepDataLootFunctionSerializer)
    )
  }
}