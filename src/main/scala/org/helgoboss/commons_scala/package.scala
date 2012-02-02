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
