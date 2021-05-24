package net.bdew.lib.render.connected

import net.minecraft.util.math.vector.Vector3i
import net.minecraftforge.client.model.data.ModelProperty

/**
 * Used for rendering borders.
 * Vectors should be (-1,-1,-1) to (+1,+1,+1) excluding the block itself (0,0,0)
 * Value is true if the block is part of the same structure
 */
object ConnectionsProperty extends ModelProperty[Map[Vector3i, Boolean]]