package org.helgoboss.commons_scala

import java.io.File
import collection.mutable.Stack

/**
 * A very flexible iterator for intelligently iterating over a directory tree. Usually created
 * by implicit conversions.
 *
 * == Example ==
 * {{{
 * scala> import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala> import java.io.File
 * import java.io.File
 *
 * scala> new File(".").tree.take(5) map { _.path } toList
 * res0: List[org.helgoboss.commons_scala.Path] = List(, LICENSE, pom.xml, src, src/main)
 * }}}
 *
 * The iterator is flexible because you can influence its behavior during the iteration.
 * For example, you can tell him not to recurse deeper after having visited a particular node by setting the
 * `recurse` property on [[org.helgoboss.commons_scala.Node]]. Or you can tell him to walk over
 * a certain directory in a special order by setting the `walkOrder` property on
 * [[org.helgoboss.commons_scala.Node]]. Or you can tell him not to walk any further over a directory level
 * by setting the property `continue` on [[org.helgoboss.commons_scala.Level]].
 *
 * Have a look at the source code of [[org.helgoboss.commons_scala.RichNodeIterator]] and
 * [[org.helgoboss.commons_scala.PatternCriteria]] to see how these features can be used to implement efficient and
 * convenient file inclusion and exclusion filters like shown in the following example.
 *
 * '''Please ignore the backslashes!'''
 * {{{
 * scala>  import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala>  import java.io.File
 * import java.io.File
 *
 * scala>  new File(".").tree.include("**\/\*.scala").exclude("**\/.svn", "**\/.svn/\**").toList
 * res0: List[org.helgoboss.commons_scala.Node] = List()
 * }}}
 *
 * The `include` and `exclude` methods are provided by [[org.helgoboss.commons_scala.RichNodeIterator]].
 * Of course, you can combine these methods easily with `filter` and `map`. These are all just methods
 * taking iterators as input and producing iterators as output. Chain them together and you can elegantly
 * express filtering and transformation without sacrificing performance or increasing memory usage -
 * it all just boils down to chains of method calls on iterators.
 *
 * @constructor Creates an iterator for recursive iteration over a root directory in the given order, if any.
 * @param root root directory (will itself be part of the elements returned by `next`)
 * @param defaultWalkOrder optional default walk order, can be overridden by node walk orders, see
 *  [[org.helgoboss.commons_scala.Node]] for further explanations
 */
class FileTreeIterator(root: File, defaultWalkOrder: Option[(File, File) => Boolean] = None)
  extends Iterator[Node] {
  private var recurseVar: Boolean = _
  private var walkOrderVar: Option[(File, File) => Boolean] = _
  private var previousNode: Option[Node] = None
  private var nextNode: Option[Node] = None
  private val levelStack = new Stack[InternalLevel]

  resetInstructions()
  setAsNextNode(root)

  def hasNext: Boolean = {
    determineNextNodeIfNotAlreadyDone()
    !nextNode.isEmpty
  }

  def next(): Node = {
    determineNextNodeIfNotAlreadyDone()
    if (nextNode.isEmpty) {
      null
    } else {
      previousNode = nextNode
      nextNode = None
      previousNode.get
    }
  }


  private def determineNextNodeIfNotAlreadyDone() {
    if (nextNode.isEmpty) {
      determineNextNode()
    }
  }


  private def determineNextNode() {
    var stackCompletelyProcessed = false

    /* Recurse deeper if recursing is enabled */

    if (previousNode.get.file.isDirectory && recurseVar) {
      val level = new InternalLevel {
        val dir = previousNode.get.file

        val childrenIterator = {
          val children = dir.listFiles
          walkOrderVar match {
            case Some(x) => children.sortWith(x).iterator
            case None => children.iterator
          }
        }
      }
      levelStack.push(level)
    }

    /* Reset recurse and walkOrder instructions so they don't have a global effect */

    resetInstructions()


    /* Search next file in current level. If no more file is left on this level, go one level up, and so on. */

    do {
      if (levelStack.isEmpty) {
        /* Complete iteration finished. */
        stackCompletelyProcessed = true
      } else {
        /* There's something on the level stack to be processed */
        val currentLevel = levelStack.top
        val childrenIterator = currentLevel.childrenIterator
        if (currentLevel.continue && childrenIterator.hasNext) {
          /* Not all directory entries returned yet. Return next file in current directory. */
          val file = childrenIterator.next()
          if (file.getName == "..") {
            /* Ignore */
          } else {
            setAsNextNode(file)
          }
        } else {
          /* All directory entries processed. Go to parent directory */
          levelStack.pop()
        }
      }
    } while (nextNode.isEmpty && !stackCompletelyProcessed)
  }

  private def setAsNextNode(nextFile: File) {
    val node = new Node {
      val levels = levelStack.toList.reverse

      val file = nextFile

      val path = Path(
        if (levels.isEmpty) {
          /* This is the root. Occurs only at the first iteration. Its path expression is "" */
          Nil
        } else if (levels.size == 1) {
          /* This is a direct sub file of the root. Its path expression is "x" */
          List(file.getName)
        } else {
          /* This is a transitive sub file of the root. Its path expression is "x/..." */
          (levels.tail map { _.dir.getName } toList) :+ file.getName
        }
      )

      def recurse_=(value: Boolean) {
        recurseVar = value
      }

      def recurse = recurseVar

      def walkOrder_=(walkOrder: Option[(File, File) => Boolean]) {
        walkOrderVar = walkOrder
      }

      def walkOrder = walkOrderVar
    }

    nextNode = Some(node)
  }

  private def resetInstructions() {
    recurseVar = true
    walkOrderVar = defaultWalkOrder
  }


  private trait InternalLevel extends Level {
    var continue = true
    def childrenIterator: Iterator[File]
  }

}
