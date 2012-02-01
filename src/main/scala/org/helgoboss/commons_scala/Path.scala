package org.helgoboss.commons_scala

import MatchResult._

/**
 * Factory for creating [[org.helgoboss.commons_scala.Path]]s.
 *
 * == Example ==
 * {{{
 * scala>  import org.helgoboss.commons_scala.Path
 * import org.helgoboss.commons_scala.Path
 *
 * scala>  val p = Path("src/main/scala")
 * p: org.helgoboss.commons_scala.Path = src/main/scala
 *
 * scala>  val p2 = Path(List("src", "main", "scala"))
 * p2: org.helgoboss.commons_scala.Path = src/main/scala
 *
 * scala>  val p3 = Path("src/main/java/../scala")
 * p3: org.helgoboss.commons_scala.Path = src/main/scala
 *
 * scala>  val p4 = Path("//src/main////scala/./")
 * p4: org.helgoboss.commons_scala.Path = src/main/scala
 * }}}
 */
object Path {
  /**
   * Makes it possible to pass a String to every method which expects a `Path` as argument. In this case,
   * the String is just interpreted as a path expression and converted to a `Path`.
   *
   * Import of this implicit method is not necessary, works without!
   */
  implicit def string2Path(pathExpression: String) = Path(pathExpression)

  /**
   * Creates a path from the given path expression. The same normalizations are applied as in
   * the ather `apply` method.
   *
   * @param pathExpression supposed to be a String whose components are separated by slashes, for example
   * `src/main/scala`
   * @return created path
   */
  def apply(pathExpression: String): Path = apply(splitIntoComponents(pathExpression))

  /**
   * Creates a path from the given path components. Components containing only `..` remove their left neighbour as
   * you would expect from file paths. Empty components and components only containing a dot are eliminated.
   *
   * @param components list of path components
   * @return created path
   */
  def apply(components: Iterable[String]): Path = new Path(normalizeComponents(components))


  private def splitIntoComponents(pathExpression: String) = {
    pathExpression.split("/")
  }

  private def normalizeComponents(pathComponents: Iterable[String]) = {
    val normalizedComponents = new collection.mutable.Stack[String]
    pathComponents.foreach { c =>
      if (!c.isEmpty && c != ".") {
        if (c == "..") {
          if (!normalizedComponents.isEmpty) {
            normalizedComponents.pop()
          }
        } else {
          normalizedComponents.push(c)
        }
      }
    }
    normalizedComponents.toList.reverse
  }
}

/**
 * Represents a path, for example a file path or URL path.
 * A path basically is of a sequence of strings plus some behavior typically
 * associated with paths, such as path pattern matching, parsing and creation of path 
 * expressions (using slash as separator) and support for the special meaning of `.` and `..`.
 *
 * Paths are created using the companion object.
 *
 * == Example ==
 * '''Please ignore the backslashes!'''
 *
 * {{{
 * scala>  val p1 = Path("src/main//java/.././scala")
 * p1: org.helgoboss.commons_scala.Path = src/main/scala
 *
 * scala>  p1.components
 * res0: List[String] = List(src, main, scala)
 *
 * scala>  p1 / "org/helgoboss"
 * res1: org.helgoboss.commons_scala.Path = src/main/scala/org/helgoboss
 *
 * scala>  p1 matches "src/\**\/sc*la"
 * res2: Boolean = true
 * }}}
 *
 * Please note that the concept of absolute and relative path is not included for keeping things simple. I
 * consider this concept to be already a concrete application of paths. If you want that, it should be easy to
 * add this functionality in the client as a decoration.
 *
 * @constructor Creates a path with the given components.
 * @param components Returns a normalized list of path components. Path components are those strings which are separated by slashes
 *   in the path expression.
 */
class Path protected(val components: List[String]) {
  /**
   * Returns the slash expression of the path.
   */
  override def toString = components.mkString("/")

  /**
   * Returns whether this path matches the given pattern. This is just a convenience method. Use
   * `matchAgainst` on [[org.helgoboss.commons_scala.PathPattern]] in order to distinguish between more than just
   * "matches" and "not matches".
   *
   * @param pathPattern pattern
   * @return `true` if it matches, `false` otherwise
   */
  def matches(pathPattern: PathPattern): Boolean = pathPattern.matchAgainst(this) match {
    case Matches | WillMatchEverySubPath => true
    case _ => false
  }

  override def hashCode(): Int = components.hashCode()

  /**
   * Returns a new path created by appending the given right path to this one. Should be invoked as infix operator.
   *
   * @param rightPath path to append
   * @return new path
   */
  def /(rightPath: Path): Path = new Path(components ++ rightPath.components)

  override def equals(other: Any) = other match {
    case otherPath: Path => this.components == otherPath.components
    case _ => false
  }
}
