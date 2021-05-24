package net.bdew.lib.multiblock.item

import net.bdew.lib.Text._
import net.bdew.lib.items.TooltipBlockItem
import net.bdew.lib.multiblock.MultiblockMachineManager
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.{BlockItem, Item, ItemStack}
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class ModuleBlockItem(block: BlockModule[_], props: Item.Properties, machines: MultiblockMachineManager) extends BlockItem(block, props) with TooltipBlockItem {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[ITextComponent] = {
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
