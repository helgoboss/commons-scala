package org.helgoboss.commons_scala

import MatchResult._

/**
 * Factory for creating [[org.helgoboss.commons_scala.PatternCriteria]]s.
 *
 * == Example ==
 * {{{
 * scala>  import org.helgoboss.commons_scala.PatternCriteria
 * import org.helgoboss.commons_scala.PatternCriteria
 *
 * scala>  PatternCriteria(includes = Set("*.scala"), excludes = Set("*.java"))
 * res0: org.helgoboss.commons_scala.PatternCriteria = PatternCriteria(includes = Set(*.scala), excludes = Set(*.java))
 * }}}
 */
object PatternCriteria {
  /**
   * Creates the pattern criteria with the given include and exclude patterns.
   *
   * @param includes set of include patterns
   * @param excludes set of exclude patterns
   * @return pattern criteria
   */
  def apply(includes: Set[PathPattern], excludes: Set[PathPattern]): PatternCriteria = {
    new PatternCriteria(includes = includes, excludes = excludes)
  }

  /**
   * Creates the pattern criteria with the given include and exclude pattern expressions. Convenience method.
   *
   * @param includes include pattern expressions, defaults to empty
   * @param excludes exclude pattern expressions, defaults to empty
   * @return pattern criteria
   */
  def apply(includes: Iterable[String] = Set.empty, excludes: Iterable[String] = Set.empty): PatternCriteria = {
    new PatternCriteria(
      includes = includes map { PathPattern.apply } toSet,
      excludes = excludes map { PathPattern.apply } toSet
    )
  }
}

/**
 * Allows you to define [[org.helgoboss.commons_scala.PathPattern]]s and apply them to a
 * [[org.helgoboss.commons_scala.FileTreeIterator]] as inclusion and/or exclusion filters.
 *
 * '''Best practice:''' Please note that the preferred way of defining inclusion and exclusion filters is using
 * implicits. This is shown in the Scaladoc for [[org.helgoboss.commons_scala.FileTreeIterator]]. Direct use
 * of [[org.helgoboss.commons_scala.PatternCriteria]] is encouraged only if you don't want to use implicits
 * or have advanced use cases.
 *
 * == Example ==
 *
 * '''Please ignore the backslashes!'''
 *
 * Following example shows an inclusion filter.
 * {{{
 * scala>  import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala>  import java.io.File
 * import java.io.File
 *
 * scala>  val c = PatternCriteria(includes = Set("src/\*\/\*.scala"))
 * c: org.helgoboss.commons_scala.PatternCriteria = PatternCriteria(includes = Set(src/\*\/\*.scala), excludes = Set())
 *
 * scala>  new File(".").tree filter { c isMetBy _ } toList
 * res2: List[org.helgoboss.commons_scala.FileTreeIterator.Node] = List()
 * }}}
 *
 * Yes, inclusion and exclusion filters on a [[org.helgoboss.commons_scala.FileTreeIterator]]
 * could be easily applied by just combining `filter` and [[org.helgoboss.commons_scala.Path]]`.matches()`.
 * But it's another story to make this efficient. [[org.helgoboss.commons_scala.PatternCriteria]] uses the advanced
 * features of [[org.helgoboss.commons_scala.FileTreeIterator]] (in particular by preventing iteration through
 * other directories than `src`) for achieving this.
 *
 * Following example shows an inefficient ad-hoc inclusion filter. It's inefficient because the iterator won't
 * just search within the `src` directory and its subdirectories but also in all the directories which are
 * siblings of `src`.
 *
 * {{{
 * scala>  import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala>  import java.io.File
 * import java.io.File
 *
 * scala>  new File(".").tree filter { _.path matches "src\/\*\/\*.scala" } toList
 * res0: List[org.helgoboss.commons_scala.FileTreeIterator.Node] = List()
 * }}}
 *
 *
 * PatternCriteria is created using the companion object.
 */
class PatternCriteria protected(includes: Set[PathPattern], excludes: Set[PathPattern]) {
  protected trait Completeness
  protected case object Neither extends Completeness
  protected case object IncludesOnly extends Completeness
  protected case object ExcludesOnly extends Completeness
  protected case object Both extends Completeness

  protected lazy val completeness = {
    if (includes.isEmpty && excludes.isEmpty)
      Neither
    else if (includes.isEmpty && !excludes.isEmpty)
      ExcludesOnly
    else if (!includes.isEmpty && excludes.isEmpty)
      IncludesOnly
    else
      Both
  }

  /**
   * Returns whether the given node meets this criteria. It also might instruct the iterator not to
   * recurse further down the directory tree if the criteria cannot be met anyway in sub directories.
   *
   * @param node node to check
   * @return `true` if the node meets the criteria, `false` otherwise
   */
  def isMetBy(node: Node) = {
    lazy val includePatternsMatchResult = includes map { _ matchAgainst node.path }
    lazy val excludePatternsMatchResult = excludes map { _ matchAgainst node.path }

    lazy val nodeMatchesIncludePatterns = includePatternsMatchResult exists { _ == Matches }
    lazy val nodeMatchesExcludePatterns = excludePatternsMatchResult exists { _ == Matches }

    lazy val subNodeCanMatchIncludePatterns = node.file.isDirectory &&
      (includePatternsMatchResult exists { _ != NeverMatches })

    lazy val subNodeDefinitelyMatchesExcludePatterns = node.file.isDirectory &&
      (excludePatternsMatchResult exists { _ == WillMatchEverySubPath })

    completeness match {
      case Neither =>
        node.recurse = true
        true

      case IncludesOnly =>
        node.recurse = subNodeCanMatchIncludePatterns
        nodeMatchesIncludePatterns

      case ExcludesOnly =>
        node.recurse = !subNodeDefinitelyMatchesExcludePatterns
        !nodeMatchesExcludePatterns

      case Both =>
        node.recurse = subNodeCanMatchIncludePatterns && !subNodeDefinitelyMatchesExcludePatterns
        nodeMatchesIncludePatterns && !nodeMatchesExcludePatterns
    }
  }

  override def toString =
    "PatternCriteria(includes = " + includes + ", excludes = " + excludes + ")"

}
