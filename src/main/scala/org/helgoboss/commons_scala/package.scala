package org.helgoboss

/**
 * Provides several additions to the Scala standard library.
 *
 * == Highlights ==
 *
 * <ul>
 *   <li>
 *     cope with paths and Ant-style path patterns as described in
 *     [[org.helgoboss.commons_scala.Path]] and [[org.helgoboss.commons_scala.PathPattern]], for example:
 *     {{{
 *     scala> Path("src/main/scala") matches "src/main/sc*"
 *     res0: Boolean = true
 *     }}}
 *   </li>
 *   <li>
 *     dive into directory trees in convenient but flexible ways as described in
 *     [[org.helgoboss.commons_scala.FileTreeIterator]], for example:
 *     {{{
 *     scala> new File(".").tree map { _.path } toList
 *     res0: List[org.helgoboss.commons_scala.Path] = List(pom.xml, src, src/main/, src/main/scala)
 *     }}}
 *   </li>
 * </ul>
 *
 * == Miscellaneous ==
 *
 * <ul>
 *   <li>
 *     cope with camel-case Strings as described in
 *     [[org.helgoboss.commons_scala.CamelCaseString]]
 *   </li>
 *   <li>
 *     define and create directory/file trees as described in
 *     [[org.helgoboss.commons_scala.FileTree]]
 *   </li>
 *   <li>
 *     query info about the current operating system as described in
 *     [[org.helgoboss.commons_scala.CurrentPlatform]]
 *   </li>
 *   <li>
 *     pass around a standard logging interface for being able to plug arbitrary logger implementations
 *     into your module as described in
 *     [[java.util.logging.Logger]]
 *   </li>
 *   <li>
 *     cope with nested maps as described in
 *     [[org.helgoboss.commons_scala.NestedMap]]
 *   </li>
 *   <li>
 *     use some convenience methods for coping with files as described in
 *     [[org.helgoboss.commons_scala.RichFile]]
 *   </li>
 * </ul>
 *
 */
package object commons_scala {
}
