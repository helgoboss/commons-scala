/**
 * The MIT License
 *
 * Copyright (c) 2011 Benjamin Klum
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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