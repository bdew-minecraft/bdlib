package net.bdew.lib.container.switchable

import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.network.misc.{MiscNetworkHandler, MsgSwitchContainer}

case class ContainerMode(id: String)

trait SwitchableContainer extends NoInvContainer {
  private var activeMode: ContainerMode = SwitchableContainer.NormalMode
  private var registeredModes: Map[String, ContainerMode] = Map(SwitchableContainer.NormalMode.id -> SwitchableContainer.NormalMode)

  def addMode(mode: ContainerMode): Unit = registeredModes += mode.id -> mode

  def activateModeClient(mode: ContainerMode): Unit = {
    require(registeredModes.get(mode.id).contains(mode))
    activeMode = mode
    MiscNetworkHandler.sendToServer(MsgSwitchContainer(mode.id))
  }

  def activateModeRemote(mode: String): Boolean = {
    registeredModes.get(mode) match {
      case Some(m) =>
        activeMode = m
        true
      case None =>
        false
    }
  }

  def getActiveMode: ContainerMode = activeMode
}

object SwitchableContainer {
  val NormalMode: ContainerMode = ContainerMode("normal")
}
