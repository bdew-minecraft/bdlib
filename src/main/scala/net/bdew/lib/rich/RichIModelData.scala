package net.bdew.lib.rich

import net.minecraftforge.client.model.data.{EmptyModelData, IModelData, ModelDataMap, ModelProperty}

class RichIModelData(val v: IModelData) extends AnyVal {
  def withData[T](property: ModelProperty[T], value: T): IModelData = {
    if (v == EmptyModelData.INSTANCE) {
      val tmp = new ModelDataMap.Builder().build()
      tmp.setData(property, value)
      tmp
    } else {
      v.setData(property, value)
      v
    }
  }

  def withData[T](vals: Iterable[(ModelProperty[T], T)]): IModelData = {
    vals.foldLeft(if (v == EmptyModelData.INSTANCE) new ModelDataMap.Builder().build() else v) {
      case (state, (prop, value)) =>
        state.setData(prop, value)
        state
    }
  }

  def getDataOpt[T](prop: ModelProperty[T]): Option[T] =
    if (v.hasProperty(prop))
      Option(v.getData(prop))
    else
      None
}
