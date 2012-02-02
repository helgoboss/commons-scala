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
 * Represents a result of a matching operation. Used by [[org.helgoboss.commons_scala.PathPattern]].
 */
sealed trait MatchResult

/**
 * Contains all possible match results.
 */
object MatchResult {
  /**
   * Returned if neither the given path matches the pattern nor any sub path can ever match it.
   */
  case object NeverMatches extends MatchResult

  /**
   * Returned if the given path matches the pattern.
   */
  case object Matches extends MatchResult

  /**
   * Returned if the given path doesn't match the pattern but if a sub path might match it.
   */
  case object CanMatchSubPath extends MatchResult

  /**
   * Returned if the given path matches the pattern and every sub path would match it as well.
   */
  case object WillMatchEverySubPath extends MatchResult
}
