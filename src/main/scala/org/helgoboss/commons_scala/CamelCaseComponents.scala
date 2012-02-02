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
 * `Iterable[String]` wrapper which provides methods related to the
 * <a href="http://en.wikipedia.org/wiki/CamelCase">camel-case</a> writing style. Usually created
 * by implicit conversions.
 *
 * == Example ==
 * {{{
 * scala> import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala> List("MY", "camel", "CaSe", "String").camelCaseString
 * res0: java.lang.String = mYCamelCaSeString
 * }}}
 *
 * @constructor Wraps the given `Iterable`.
 * @param i Iterable whose components will be interpreted as camel-case components
 */
class CamelCaseComponents(i: Iterable[String]) {
  /**
   * Concatenates the wrapped camel case components into a typical (lower-case) camel-case String.
   *
   * @return camel-case string
   */
  def camelCaseString = i.head.head.toLower + i.head.tail + (i.tail map { _.capitalize } mkString "")
}