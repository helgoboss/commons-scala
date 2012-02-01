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
