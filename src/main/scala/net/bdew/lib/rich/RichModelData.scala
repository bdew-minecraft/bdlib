package net.bdew.lib.rich

import net.minecraftforge.client.model.data.{ModelData, ModelProperty}

class RichModelData(val v: ModelData) extends AnyVal {
  def withData[T](property: ModelProperty[T], value: T): ModelData = {
    v.derive().`with`(property, value).build()
  }

  def withData[T](vals: Iterable[(ModelProperty[T], T)]): ModelData = {
    vals.foldLeft(v.derive()) {
      case (state, (prop, value)) =>
        state.`with`(prop, value)
    }.build()
  }

  def getDataOpt[T](prop: ModelProperty[T]): Option[T] =
    if (v.has(prop))
      Option(v.get(prop))
    else
      None
}
