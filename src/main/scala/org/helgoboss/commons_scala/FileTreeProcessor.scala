/**
 * The MIT License
 *
 * Copyright (c) 2010 Benjamin Klum
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
 * Defines the interface used by [[org.helgoboss.commons_scala.FileTree]] to process a directory tree
 * structure. Implement this if you want to do something else than creating or printing the defined tree.
 */
trait FileTreeProcessor {
  /**
   * Called when the defined directory is entered.
   *
   * @param name defined name of the directory
   */
  def enterDir(name: String)

  /**
   * Called when the current directory is left.
   */
  def leaveDir()

  /**
   * Called when a file is defined with a content.
   *
   * @param name defined name of the file
   * @param content defined content of the file
   */
  def createFile(name: String, content: String)

  /**
   * Called when a file or directory is defined with a custom function.
   *
   * @param name defined name of the file
   * @param f custom function to be executed on the `File` object
   */
  def createArbitrary(name: String, f: File => Any)
}
