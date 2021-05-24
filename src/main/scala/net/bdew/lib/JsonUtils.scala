package net.bdew.lib

import com.google.gson.{JsonArray, JsonElement, JsonObject}
import net.minecraft.util.ResourceLocation

import scala.jdk.CollectionConverters.IterableHasAsScala

object JSObj {
  def unapply(js: JsonElement): Option[JsonObject] =
    if (js.isJsonObject)
      Some(js.getAsJsonObject)
    else
      None
}

object JSArray {
  def unapply(js: JsonElement): Option[JsonArray] =
    if (js != null && js.isJsonArray)
      Some(js.getAsJsonArray)
    else
      None
}

object JSObjectsArray {
  def unapply(js: JsonElement): Option[Iterable[JsonObject]] =
    if (js != null && js.isJsonArray) {
      val arr = js.getAsJsonArray
      if (arr.asScala.forall(_.isJsonObject))
        Some(js.getAsJsonArray.asScala.map(_.getAsJsonObject))
      else
        None
    } else None
}

object JSSinglePair {
  def unapply(js: JsonObject): Option[(String, JsonElement)] = {
    if (js != null && js.size() == 1) {
      js.entrySet().asScala.headOption.map(e => e.getKey -> e.getValue)
    } else None
  }
}

object JSString {
  def unapply(js: JsonElement): Option[String] =
    if (js != null && js.isJsonPrimitive && js.getAsJsonPrimitive.isString)
      Some(js.getAsString)
    else
      None
}

object JSResLoc {
  def unapply(js: JsonElement): Option[ResourceLocation] =
    if (js != null && js.isJsonPrimitive && js.getAsJsonPrimitive.isString)
      Some(new ResourceLocation(js.getAsString))
    else
      None
}

object JSInt {
  def unapply(js: JsonElement): Option[Int] =
    if (js != null && js.isJsonPrimitive && js.getAsJsonPrimitive.isNumber)
      Some(js.getAsInt)
    else
      None
}

object JSDouble {
  def unapply(js: JsonElement): Option[Double] =
    if (js != null && js.isJsonPrimitive && js.getAsJsonPrimitive.isNumber)
      Some(js.getAsDouble)
    else
      None
}