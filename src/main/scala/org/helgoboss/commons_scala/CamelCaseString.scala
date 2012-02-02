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
 * `String` wrapper which provides methods related to the
 * [[http://en.wikipedia.org/wiki/CamelCase camel-case]] writing style. Usually created
 * by implicit conversions.
 *
 * == Example ==
 * {{{
 * scala> import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala> "myCamelCaseString".camelCaseComponents
 * res0: Iterable[String] = ListBuffer(my, camel, case, string)
 * }}}
 *
 * @constructor Wraps the given string.
 * @param s string in camel-case writing style
 */
class CamelCaseString(s: String) {
  /**
   * Returns the camel-case components of the camel-case string.
   */
  def camelCaseComponents: Iterable[String] = {
    val currentComponent = new StringBuilder
    val components = new collection.mutable.ListBuffer[String]


    /* Closure for finishing a component. Only adds component if it's not empty. */
    def finishComponent() {
      if (!currentComponent.isEmpty) {
        components += currentComponent.toString
        currentComponent.clear()
      }
    }

    /* Iterate over characters and build components */
    s foreach {
      case upperCaseLetter if upperCaseLetter >= 'A' && upperCaseLetter <= 'Z' =>
        finishComponent()
        currentComponent += upperCaseLetter.toLower

      case other @ _ =>
        currentComponent += other
    }

    /* Finish last component */
    finishComponent()

    components
  }
}