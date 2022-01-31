package net.bdew.lib.multiblock.item

import net.bdew.lib.Text._
import net.bdew.lib.items.TooltipBlockItem
import net.bdew.lib.multiblock.MultiblockMachineManager
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{BlockItem, Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class ModuleBlockItem(block: BlockModule[_], props: Item.Properties, machines: MultiblockMachineManager) extends BlockItem(block, props) with TooltipBlockItem {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] = {
    super.getTooltip(stack, world, flags) :+ translate("bdlib.multiblock.tip.module") :++ (
      for ((machine, (min, max)) <- machines.getMachinesForBlock(block)) yield {
        val name = machines.resources.getMachineName(machine)
        if (min > 0) {
          translate("bdlib.multiblock.tip.module.range",
            name.setColor(Color.YELLOW),
            string(min.toString).setColor(Color.YELLOW),
            string(max.toString).setColor(Color.YELLOW)
          )
        } else {
          translate("bdlib.multiblock.tip.module.max",
            name.setColor(Color.YELLOW),
            string(max.toString).setColor(Color.YELLOW)
          )
        }
      })
  }
}
