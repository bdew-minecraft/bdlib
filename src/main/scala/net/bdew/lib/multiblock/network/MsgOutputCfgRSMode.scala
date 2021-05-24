package net.bdew.lib.multiblock.network

import net.bdew.lib.misc.RSMode

case class MsgOutputCfgRSMode(rsMode: RSMode.Value) extends MsgOutputCfgPayload
