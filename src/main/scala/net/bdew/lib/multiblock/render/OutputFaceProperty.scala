package net.bdew.lib.multiblock.render

import net.minecraft.util.Direction
import net.minecraftforge.client.model.data.ModelProperty

/**
 * Used for rendering output overlays, the integer will be used as tint index for coloring
 */
object OutputFaceProperty extends ModelProperty[Map[Direction, Int]]
