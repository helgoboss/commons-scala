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

import MatchResult._

/**
 * Factory for creating [[org.helgoboss.commons_scala.PathPattern]]s.
 */
object PathPattern {
  /**
   * Creates a path pattern from the given path pattern expression.
   *
   * @param pathPatternExpression Ant-style path pattern expression, for example `**\/\*.scala`
   *   ('''Please ignore the backslashes!''')
   * @return created path pattern
   */
  def apply(pathPatternExpression: String): PathPattern = new PathPattern(pathPatternExpression)

  /**
   * Makes it possible to pass a String to every method which expects a
   * [[org.helgoboss.commons_scala.PathPattern]] as argument. In this case,
   * the String is just interpreted as a path pattern expression and converted to a
   * [[org.helgoboss.commons_scala.PathPattern]].
   *
   * Import of this implicit method is not necessary, works without!
   */
  implicit def string2PathPattern(expression: String) = PathPattern(expression)
}

/**
 * Represents an Ant-style path pattern. Path patterns are created using the companion object.
 *
 *
 * == Example ==
 * '''Please ignore the backslashes!'''
 *
 * {{{
 * scala>  import org.helgoboss.commons_scala.PathPattern
 * import org.helgoboss.commons_scala.PathPattern
 *
 * scala>  val pp = PathPattern("src/\**")
 * pp: org.helgoboss.commons_scala.PathPattern = src/\**
 *
 * scala>  pp matchAgainst "src"
 * res0: org.helgoboss.commons_scala.MatchResult = WillMatchEverySubPath
 *
 * scala>  pp matchAgainst "target"
 * res1: org.helgoboss.commons_scala.MatchResult = NeverMatches
 *
 * scala>  val pp2 = PathPattern("src/\**\/\*.scala")
 * pp2: org.helgoboss.commons_scala.PathPattern = src/\**\/\*.scala
 *
 * scala>  pp2 matchAgainst "src/code.scala"
 * res2: org.helgoboss.commons_scala.MatchResult = Matches
 *
 * scala>   pp2 matchAgainst "src/test"
 * res3: org.helgoboss.commons_scala.MatchResult = CanMatchSubPath
 * }}}
 */
class PathPattern protected(expression: String) {
  /**
   * Internally, the path pattern is implemented as a path because it shows a lot of similarities.
   */
  private val internalPath = Path(expression)

  /**
   * Matches this pattern against the given path.
   *
   * @param path path to match against
   * @return result of the match operation
   */
  def matchAgainst(path: Path): MatchResult = {
    val patternIter = internalPath.components.iterator
    val pathWithIndexIter = path.components.zipWithIndex.iterator

    var maybeStillCanMatch = true
    var matchesBecauseOfOpenEnd = false
    var canMatchSubPathBecauseOfOpenEnd = false

    while (maybeStillCanMatch && patternIter.hasNext && pathWithIndexIter.hasNext) {
      val patternComponent = patternIter.next()
      val pathWithIndex = pathWithIndexIter.next()
      val pathComponent = pathWithIndex._1
      val pathIndex = pathWithIndex._2

      if (patternComponent == "**") {
        /* Component-skipping pattern component. Search in next path components for the first one that matches the
           current pattern component. */
        if (patternIter.hasNext) {
          /* The stars are inside the pattern. Find the last path component which matches the pattern
             component after the double stars (greedy behavior). */
          var patternComponentAfterDoubleStars = patternIter.next()

          val optionalLastMatchIndex = findIndexOfLastMatchingPathComponent(
            path = path,
            patternComponent = patternComponentAfterDoubleStars,
            fromIndex = pathIndex
          )
          optionalLastMatchIndex match {
            case Some(lastMatchIndex) =>
              /* Found a path component which matches the pattern component after the double stars. If there
                 are several, it found the last matching path one. Set the path iterator to its position
                 and continue from there. */
              val distanceToMatch = lastMatchIndex - pathIndex
              pathWithIndexIter.drop(distanceToMatch)
            case None =>
              /* Couldn't find any path component which matches the pattern component after the double stars.
                 This doesn't match. But a sub path could. */
              canMatchSubPathBecauseOfOpenEnd = true
          }
        } else {
          /* This is the last pattern component. */
          matchesBecauseOfOpenEnd = true
        }
      } else {
        /* Normal pattern component */

        /* Check whether the next actual path component matches the current pattern component */
        if (componentsMatch(pathComponent, patternComponent)) {
          /* They match. Move on. */
        } else {
          /* They don't match and hence cannot match in a sub tree either */
          maybeStillCanMatch = false
        }
      }
    }



    /* Summarize the result as MatchResult  */
    if (matchesBecauseOfOpenEnd) {
      /* The pattern's last component is ** and the actual path matches the pattern because of that. */
      Matches
    } else if (canMatchSubPathBecauseOfOpenEnd) {
      /* The actual path suddenly ended when looking for the right delimiter of ** in the pattern. Because of that,
         the pattern could match a sub path. */
      CanMatchSubPath
    } else if (maybeStillCanMatch) {
      /* All compared components match successfully. At this point, the pattern, the actual path or both have been
         fully examined. */
      if (!patternIter.hasNext && !pathWithIndexIter.hasNext) {
        /* Both pattern and actual path have been fully examined. This means, it's a perfect match. */
        Matches
      } else if (!patternIter.hasNext && pathWithIndexIter.hasNext) {
        /* Pattern has been fully examined. Actual path goes on. */
        NeverMatches
      } else {
        /* Actual path has been fully examined. Pattern goes on. So the pattern could potentially match in a sub path
           of the actual path. */
        if (patternIter.next == "**" && !patternIter.hasNext) {
          /* The pattern matches AND it matches every sub path because the next and last pattern component is two asterisks. */
          WillMatchEverySubPath
        } else {
          /* The pattern could match a sub path. */
          CanMatchSubPath
        }
      }
    } else {
      /* In at least one component, pattern and actual path don't match. That means, pattern and actual path don't
         match at all. */
      NeverMatches
    }
  }

  /**
   * Finds the last path component which matches the given pattern component. It searches through the
   * path components from end to beginning (reverse) but looks no further than to `fromIndex`.
   */
  private def findIndexOfLastMatchingPathComponent(path: Path, patternComponent: String, fromIndex: Int): Option[Int] = {
    val optionalComponentWithIndex = path.components.zipWithIndex.drop(fromIndex).reverse find { case (c, i) =>
      componentsMatch(c, patternComponent)
    }
    optionalComponentWithIndex.map(_._2)
  }

  /**
   * Checks whether the given path component matches the given pattern component.
   */
  private def componentsMatch(pathComponent: String, patternComponent: String) = {
    /* Transforms the wildcard expression of the pattern into a real regular expression by escaping the
       dot and replacing the asterisk with a dot and an asterisk.  */
    val patternComponentRegexString = patternComponent
      .replace(".", """\.""")
      .replace("*", ".*")
    pathComponent matches patternComponentRegexString
  }

  override def toString = internalPath.toString
}
