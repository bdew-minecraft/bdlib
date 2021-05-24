package net.bdew.lib.multiblock.network

trait MsgOutputCfgPayload extends Serializable

case class MsgOutputCfg(output: Int, payload: MsgOutputCfgPayload) extends MultiblockNetHandler.Message
