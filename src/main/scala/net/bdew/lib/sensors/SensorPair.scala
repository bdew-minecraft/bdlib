package net.bdew.lib.sensors

case class SensorPair[-T, +R](sensor: GenericSensorType[T, R], param: GenericSensorParameter) {
  def getResult(obj: T): R = sensor.getResult(param, obj)
}
