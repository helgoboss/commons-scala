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

import java.io.File


/**
 * Represents a node in the file tree. Used by [[org.helgoboss.commons_scala.FileTreeIterator]].
 */
trait Node {
  /**
   * Returns the `File` object pointing to the currently visited file or directory.
   */
  def file: File

  /**
   * Returns the path of the currently visited file or directory relative to the root directory.
   */
  def path: Path

  /**
   * Returns the current directory stack without the currently processed file or directory.
   *
   * First element is root directory, last element is the currently walked directory.
   */
  def levels: List[Level]

  /**
   * Sets whether the iterator shall recurse deeper.
   *
   * @param value `true` if it should recurse deeper, `false` otherwise
   */
  def recurse_=(value: Boolean)


  /**
   * Returns whether the iterator will recurse deeper.
   *
   * @return `true` if it will recurse deeper, `false` otherwise
   */
  def recurse: Boolean

  /**
   * Sets the optional walk order for the next recursion.
   *
   * @param walkOrder optional function returning `true` if and only if the first file should be walked
   *   prior to the second file.
   */
  def walkOrder_=(walkOrder: Option[(File, File) => Boolean])

  /**
   * Returns the walk order for the next recursion.
   */
  def walkOrder: Option[(File, File) => Boolean]
}