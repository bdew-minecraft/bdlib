package net.bdew.lib.multiblock.item

import net.bdew.lib.Text._
import net.bdew.lib.items.TooltipBlockItem
import net.bdew.lib.multiblock.block.BlockController
import net.bdew.lib.multiblock.{MultiblockMachineConfig, MultiblockMachineManager}
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{BlockItem, Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class ControllerBlockItem(block: BlockController[_], props: Item.Properties, machines: MultiblockMachineManager, machine: MultiblockMachineConfig) extends BlockItem(block, props) with TooltipBlockItem {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    super.getTooltip(stack, world, flags) :+ translate("bdlib.multiblock.tip.controller") :++ (
      for ((kind, max) <- machine.modules()) yield {
        val name = machines.resources.getModuleName(kind.id)
        machine.required().get(kind) match {
          case Some(req) if req > 0 =>
            translate("bdlib.multiblock.tip.module.range",
              name.setColor(Color.YELLOW),
              string(req.toString).setColor(Color.YELLOW),
              string(max.toString).setColor(Color.YELLOW)
            )
          case _ =>
            translate("bdlib.multiblock.tip.module.max",
              name.setColor(Color.YELLOW),
              string(max.toString).setColor(Color.YELLOW)
            )
        }
      })
}
