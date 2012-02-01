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