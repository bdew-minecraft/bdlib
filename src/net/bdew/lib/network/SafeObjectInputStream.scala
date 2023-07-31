package net.bdew.lib.network

import net.bdew.lib.BdLib

import java.io.{InputStream, InvalidClassException, ObjectInputStream, ObjectStreamClass}

class SafeObjectInputStream(is: InputStream) extends ObjectInputStream(is) {
  override def resolveClass(desc: ObjectStreamClass): Class[_] = {
    if (SafeObjectInputStream.validClasses.contains(desc.getName)) {
      super.resolveClass(desc)
    } else {
      BdLib.logWarn("Invalid class in network packet: %s", desc.getName)
      throw new InvalidClassException(desc.getName)
    }
  }
}

object SafeObjectInputStream {
  private val validClasses: Set[String] = Set(
    "scala.Enumeration",
    "scala.Enumeration$Val",
    "scala.Enumeration$Value",
    "scala.collection.mutable.HashMap",
    "scala.Enumeration$ValueSet",
    "scala.Enumeration$ValueSet$",
    "scala.collection.immutable.BitSet",
    "scala.collection.immutable.BitSet$BitSet1",
    "scala.collection.immutable.List$SerializationProxy",
    "scala.collection.immutable.ListSerializeEnd$",
    "java.lang.Float",
    "java.lang.Double",
    "java.lang.Integer",
    "java.lang.Number",
    "java.lang.Enum",
    "net.bdew.lib.multiblock.data.RSMode$",
    "[B",
    "net.bdew.lib.network.NBTTagCompoundSerialize",
    "net.bdew.lib.network.ItemStackSerialize",
    "net.bdew.lib.network.BaseMessage",
    "net.bdew.lib.multiblock.data.MsgOutputCfgRSMode",
    "net.bdew.lib.multiblock.network.MsgOutputCfg",
    "net.bdew.lib.multiblock.network.MsgOutputCfgSlot",
    "net.minecraft.util.EnumFacing",

    // ae2stuff stuff
    "net.bdew.ae2stuff.items.visualiser.VisualisationData",
    "net.bdew.ae2stuff.items.visualiser.VisualisationModes$",
    "net.bdew.ae2stuff.network.MsgSetLock",
    "net.bdew.ae2stuff.network.MsgSetRecipe",
    "net.bdew.ae2stuff.network.MsgSetRecipe2",
    "net.bdew.ae2stuff.network.MsgSetRecipe3",
    "net.bdew.ae2stuff.network.MsgVisualisationData",
    "net.bdew.ae2stuff.network.MsgVisualisationMode",

    // pressure pipes
    "net.bdew.pressure.blocks.router.gui.MsgSetRouterSideControl",
    "net.bdew.pressure.items.configurator.MsgSetFluidFilter",
    "net.bdew.pressure.items.configurator.MsgUnsetFluidFilter",
    "net.bdew.pressure.network.MsgTankUpdate",

    // advanced generators
    "net.bdew.generators.network.PktDumpBuffers",

    // compacter
    "net.bdew.compacter.network.MsgSetRecurseMode",
    "net.bdew.compacter.network.MsgSetCraftMode",
    "net.bdew.compacter.network.MsgSetRsMode",
    "net.bdew.compacter.blocks.compacter.RecurseMode$",
    "net.bdew.compacter.blocks.compacter.CraftMode",
    "net.bdew.compacter.blocks.compacter.CraftMode$"
  )
}