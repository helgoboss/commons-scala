package org.helgoboss.commons_scala

/**
 * `Map` wrapper which provides a method to easily query values from nested maps.
 * Usually created by implicit conversions.
 *
 * == Example ==
 * {{{
 * scala> import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala> val nestedMap = Map("outer_key" -> Map("inner_key" -> "inner_value"), "key" -> "value")
 * nestedMap: scala.collection.immutable.Map[java.lang.String,java.lang.Object] = Map(outer_key -> Map(inner_key -> inner_value), key -> value)
 *
 * scala> nestedMap.getDeep("outer_key", "inner_key")
 * res0: Option[Any] = Some(inner_value)
 *
 * scala> nestedMap.getDeep("outer_key", "non_existing_inner_key")
 * res1: Option[Any] = None
 *
 * scala> nestedMap.getDeep("outer_key")
 * res2: Option[Any] = Some(Map(inner_key -> inner_value))
 *
 * scala> nestedMap.getDeep("key")
 * res3: Option[Any] = Some(value)
 * }}}
 *
 * @constructor Wraps the given `Map`.
 * @param m potentially nested map
 * @tparam K common key type (it's assumed that the key type is the same on all levels)
 */
class NestedMap[K](m: Map[K, Any]) {
  /**
   * Optionally returns the value associated by the given key chain.
   */
  def getDeep(keyChain: K*): Option[Any] = getDeep(m, keyChain.iterator)

  private def getDeep(value: Any, keyIterator: Iterator[K]): Option[Any] = {
    value match {
      case m: Map[K, Any] =>
        /* The value is a map */
        if (keyIterator.hasNext) {
          /* Keys to lookup left */
          val key = keyIterator.next
          m.get(key) match {
            case Some(nextValue) => getDeep(nextValue, keyIterator)
            case None => None
          }
        } else {
          /* No more keys to lookup left */
          Some(m)
        }
      case _ =>
        /* The value is not a map */
        if (keyIterator.hasNext) {
          /* Keys to lookup left */
          None
        } else {
          /* No more keys to lookup left */
          Some(value)
        }
    }
  }
}