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
