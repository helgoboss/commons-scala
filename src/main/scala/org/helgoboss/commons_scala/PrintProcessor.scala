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

import java.io.{File, PrintWriter}


/**
 * Implementation of [[org.helgoboss.commons_scala.FileTreeProcessor]] which just prints the
 * defined file/directory tree to the specified writer.
 *
 * @constructor Creates the processor with the given writer.
 * @param out writer
 */
class PrintProcessor(out: PrintWriter) extends FileTreeProcessor {
  private val dirStack = new collection.mutable.Stack[String]

  def enterDir(name: String) {
    printContent(name)
    dirStack.push(name)
  }

  def leaveDir {
    dirStack.pop()
  }

  def createFile(name: String, content: String) {
    printContent(name)
  }

  def createArbitrary(name: String, f: (File) => Any) {
    printContent(name)
  }

  private def printContent(text: String) {
    out.println(" " * (dirStack.size * 4) + text)
  }
}
