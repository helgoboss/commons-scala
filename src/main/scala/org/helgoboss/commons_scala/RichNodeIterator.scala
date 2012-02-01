/**
 * The MIT License
 *
 * Copyright (c) 2010 Benjamin Klum
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
 * Node iterator wrapper which provides methods for convenient inclusion and exclusion of certain files
 * and directories. Internally it just uses [[org.helgoboss.commons_scala.PatternCriteria]], making it
 * very efficient. It's usually created by implicit conversions in [[org.helgoboss.commons_scala.Implicits]].
 *
 * If you want to encapsulate your own inclusion or exclusion logic, I encourage you to implement it in the same
 * way as done here. Sticking with the built-in Scala features such as iterators and implicits makes your
 * extension feel like a built-in feature.
 *
 * @see [[org.helgoboss.commons_scala.FileTreeIterator]] for example how to use it
 * @constructor Wraps the given node iterator.
 * @param it wrapped node iterator
 */
class RichNodeIterator(it: Iterator[Node]) {
  /**
   * Returns an iterator that includes only those nodes whose paths match the given path pattern expressions.
   *
   * @param patternExpressions path pattern expressions for inclusion
   * @return filtered iterator
   */
  def include(patternExpressions: String*) = {
    val patternCriteria = PatternCriteria(includes = patternExpressions.toSet)
    it filter { patternCriteria isMetBy _ }
  }

  /**
   * Returns an iterator that excludes all those nodes whose paths match the given path pattern expressions.
   *
   * @param patternExpressions path pattern expressions for exclusion
   * @return filtered iterator
   */
  def exclude(patternExpressions: String*) = {
    val patternCriteria = PatternCriteria(excludes = patternExpressions.toSet)
    it filter { patternCriteria isMetBy _ }
  }
}
