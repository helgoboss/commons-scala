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

import java.io.{File, PrintWriter}

/**
 * Provides a little DSL for defining directory trees. Extend from this class if you want to define a simple tree of
 * directories and files in a nice visual manner. You can then process the tree definition using a
 * [[org.helgoboss.commons_scala.FileTreeProcessor]]. For example, you can create the tree in a directory
 * of your choice.
 *
 * == Example ==
 * {{{
 * new FileTree {
 *   def structure() {
 *     "dir1" << {
 *     }
 *     "dir2" << {
 *       "file1" << ""
 *       "file2" << "Hello world!"
 *       "dir3" << {
 *         "file3" << ""
 *       }
 *     }
 *   }
 * }.createIn(new File("."))
 * }}}
 *
 * Note that this class is not thread safe and that the API is likely to change in future versions.
 */
abstract class FileTree {
  /**
   * Will be set to the processor processing the tree structure when calling `processWith`.
   */
  private var processor: FileTreeProcessor = _

  /**
   * Provides support for writing `"myFile.txt" << "blabla"`
   */
  implicit def stringToFileElement(s: String) = new FileElement(s)

  /**
   * Provides support for writing `new File("myFile.txt") << "blabla"`
   */
  implicit def fileToFileElement(f: File) = new FileElement(f.getPath)

  /**
   * Defines a directory or file in the DSL. Usually created by [[org.helgoboss.commons_scala.FileTree]]'s
   * implicits.
   */
  class FileElement(name: String) {
    /**
     * Defines a directory with the given content.
     *
     * @param block directory content
     */
    def <<(block: => Any) {
      processor.enterDir(name)
      block
      processor.leaveDir
    }

    /**
     * Defines a file with the given content.
     *
     * @param fileContent content
     */
    def <<(fileContent: String) {
      processor.createFile(name, fileContent)
    }

    /**
     * Defines a file on which the given function will be executed. This is for advanced stuff.
     *
     * @param f custom function
     */
    def <<(f: File => Any) {
      processor.createArbitrary(name, f)
    }
  }

  /**
   * Implement this to define the file tree.
   */
  def structure()

  /**
   * Processes the tree defined in `structure` with the given processor.
   *
   * @param processor processor
   */
  def processWith(processor: FileTreeProcessor) {
    this.processor = processor
    structure()
  }

  /**
   * Convenience method for creating the defined tree in the given directory (just uses
   * [[org.helgoboss.commons_scala.FileTreeProcessor]]).
   *
   * @param rootDir root directory
   */
  def createIn(rootDir: File) {
    processWith(new CreateProcessor(rootDir))
  }

  /**
   * Convenience method for printing the defined tree to the given writer in a human-readable manner (just uses
   * [[org.helgoboss.commons_scala.PrintProcessor]]).
   *
   * @param pw writer
   */
  def printTo(pw: PrintWriter) {
    processWith(new PrintProcessor(pw))
  }
}
