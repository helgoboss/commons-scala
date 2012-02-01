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