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
import java.io.FileWriter

/**
 * Implementation of [[org.helgoboss.commons_scala.FileTreeProcessor]] which just creates the
 * defined directory tree in the specified root directory.
 *
 * @constructor Creates the processor with the given root directory.
 * @param rootDir root directory
 */
class CreateProcessor(rootDir: File) extends FileTreeProcessor {
  private val dirStack = new collection.mutable.Stack[String]

  def enterDir(name: String) {
    new File(currentDir, name).mkdirs()
    dirStack.push(name)
  }

  def leaveDir {
    dirStack.pop()
  }

  def createFile(name: String, content: String) {
    createArbitrary(name, createFile(_, content))
  }

  def createArbitrary(name: String, f: File => Any) {
    val file = new File(currentDir, name)
    file.getParentFile.mkdirs()
    f(file)
  }

  private def createFile(file: File, content: String) {
    val fw = new FileWriter(file)
    try fw.write(content) finally fw.close()
  }

  private def currentDir = {
    new File(rootDir, dirStack.reverseIterator.mkString("/"))
  }
}
