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

import java.io.{File, InputStream}

/**
 * Import this object's members in order to bring all implicit conversions of `commons-scala` into your scope.
 *
 * == Example ==
 * {{{
 * import org.helgoboss.commons_scala.Implicits._
 * }}}
 *
 * You can also choose to import just some of it:
 * {{{
 * import org.helgoboss.commons_scala.Implicits.fileToRichFile
 * }}}
 */
object Implicits extends Implicits

/**
 * You can mix in this trait to have all implicit conversions of `commons-scala` in your class.
 *
 * Please note that the preferred way of getting access to the implicit conversions is importing from the companion
 * object.
 */
trait Implicits {
  implicit def fileToRichFile(file: File) = new RichFile(file)

  implicit def stringToCamelCaseString(s: String) = new CamelCaseString(s)

  implicit def stringIterableToCamelCaseComponents(i: Iterable[String]) = new CamelCaseComponents(i)

  implicit def nodeIteratorToRichNodeIterator(original: Iterator[Node]) = new RichNodeIterator(original)

  implicit def mapToNestedMap[K](m: Map[K, Any]) = new NestedMap(m)
}
