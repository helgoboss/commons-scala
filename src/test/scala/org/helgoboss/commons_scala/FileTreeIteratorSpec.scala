package org.helgoboss.commons_scala

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import Implicits._

@RunWith(classOf[JUnitRunner])
class FileTreeIteratorSpec extends WordSpec with ShouldMatchers {
  /* Create file tree on file system */

  val tree = new FileTree {
    def structure = {
      "dir1" << {
      }
      "dir2" << {
        "file1" << ""
        "file2" << "Hello world!"
        "dir3" << {
          "file3" << ""
        }
        "dir4" << {
          "file4" << ""
          "file5" << ""
        }
      }
    }
  }

  val tmpDir = RichFile.createTempDir(getClass.getName)
  tree.createIn(tmpDir)

  def f(path: String) = new File(tmpDir, path)

  def p(expression: String) = Path(expression)


  "A file recurser" should {
    "find all files in a given directory if not further customized" in {
      val expectedFiles = Set(
        f(""),
        f("dir1"),
        f("dir2"),
        f("dir2/file1"),
        f("dir2/file2"),
        f("dir2/dir3"),
        f("dir2/dir3/file3"),
        f("dir2/dir4"),
        f("dir2/dir4/file4"),
        f("dir2/dir4/file5")
      )
      val actualFiles = tmpDir.tree map { _.file } toSet

      assert(actualFiles === expectedFiles)
    }

    "find all paths in a given directory if not further customized" in {
      val expectedPaths = Set(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/file1"),
        p("dir2/file2"),
        p("dir2/dir3"),
        p("dir2/dir3/file3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree.map(_.path).toSet

      assert(actualPaths === expectedPaths)
    }

    "be able to find paths satisfying certain predicates" in {
      val expectedPaths = Set(
        p("dir2/file1"),
        p("dir2/file2"),
        p("dir2/dir3/file3"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree filter { !_.file.isDirectory } map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "be able to stop recursing deeper" in {
      val expectedPaths = Set(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/file1"),
        p("dir2/file2"),
        p("dir2/dir3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree.map {node =>
        if (node.path == p("dir2/dir3")) {
          node.recurse = false
        }
        node.path
      }.toSet

      assert(actualPaths === expectedPaths)
    }

    "set the recursion flag for the current node only - not globally" in {
      val expectedPaths = Set(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/file1"),
        p("dir2/file2"),
        p("dir2/dir3"),
        p("dir2/dir3/file3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree(defaultWalkOrder = Some(_.getName < _.getName)).map {node =>
        if (node.path == p("dir1")) {
          node.recurse = false
        }
        node.path
      }.toSet

      assert(actualPaths === expectedPaths)
    }

    "be able to set a walking order for every node separately" in {
      val expectedPaths = List(
        p(""),
        p("dir2"),
        p("dir2/file2"),
        p("dir2/file1"),
        p("dir2/dir4"),
        p("dir2/dir4/file5"),
        p("dir2/dir4/file4"),
        p("dir2/dir3"),
        p("dir2/dir3/file3"),
        p("dir1")
      )

      val actualPaths = tmpDir.tree.map {node =>
        node.walkOrder = Some(_.getName > _.getName)
        node.path
      }.toList

      assert(actualPaths === expectedPaths)
    }

    "be able to set a default walking order" in {
      val expectedPaths = List(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/dir3"),
        p("dir2/dir3/file3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5"),
        p("dir2/file1"),
        p("dir2/file2")
      )

      val actualPaths = tmpDir.tree(defaultWalkOrder = Some(_.getName < _.getName)) map { _.path } toList

      assert(actualPaths === expectedPaths)
    }


    "be able to set a walking order for every node separately more strict" in {
      val expectedPaths = List(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/file2"),
        p("dir2/file1"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5"),
        p("dir2/dir3"),
        p("dir2/dir3/file3")
      )

      val actualPaths = tmpDir.tree(defaultWalkOrder = Some(_.getName < _.getName)) map { node =>
        if (node.path matches "dir2")
          node.walkOrder = Some(_.getName > _.getName)
        node.path
      } toList

      assert(actualPaths === expectedPaths)
    }



    "be able to stop visiting the siblings" in {
      val expectedPaths = Set(
        p(""),
        p("dir1"),
        p("dir2"),
        p("dir2/dir3"),
        p("dir2/dir3/file3")
      )

      val actualPaths = tmpDir.tree.map {node =>
        node.walkOrder = Some(_.getName < _.getName)
        if (node.path == p("dir2/dir3")) {
          node.levels.last.continue = false
        }
        node.path
      }.toSet

      assert(actualPaths === expectedPaths)
    }
  }

  "A file recurser with file pattern support" should {

    "find paths based on one simple include pattern" in {
      val expectedPaths = Set(
        p("dir2/file2"),
        p("dir2/file1")
      )

      val patternCriteria = PatternCriteria(includes = Set("*/file*"))

      val actualPaths = tmpDir.tree filter { patternCriteria isMetBy _ } map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "find files based on multiple simple include patterns" in {
      val expectedPaths = Set(
        p("dir2/file2"),
        p("dir2/file1"),
        p("dir1")
      )

      val actualPaths = tmpDir.tree.include("*/file*", "d*1") map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "find files based on one complicated include pattern" in {
      val expectedPaths = Set(
        p("dir2/file1")
      )

      val actualPaths = tmpDir.tree.include("dir2/**/file1") map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "find files based on multiple complicated include pattern" in {
      val expectedPaths = Set(
        p("dir2/file1"),
        p("dir2/file2"),
        p("dir2/dir3"),
        p("dir2/dir3/file3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree.include("dir2/**", "dir2/file1") map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "find files based on multiple complicated exclude patterns" in {
      val expectedPaths = Set(
        p(""),
        p("dir1"),
        p("dir2/dir3"),
        p("dir2/dir4")
      )

      val actualPaths = tmpDir.tree.exclude("**/fi*le*", "dir2") map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "find files based on multiple complicated include and exclude patterns" in {
      val expectedPaths = Set(
        p("dir1"),
        p("dir2/file1"),
        p("dir2/dir3/file3"),
        p("dir2/dir4"),
        p("dir2/dir4/file4"),
        p("dir2/dir4/file5")
      )

      val actualPaths = tmpDir.tree.include("*/**", "dir1").exclude("**/dir3", "*/file2", "**/moin.txt")
        .map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

    "work together with customizations of the file recurser" in {
      val expectedPaths = Set(
        p("dir1"),
        p("dir2")
      )
      val patternCriteria = PatternCriteria(includes = Set("dir1"))

      val actualPaths = tmpDir.tree filter { node =>
        (patternCriteria isMetBy node) || node.path == Path("dir2")
      } map { _.path } toSet

      assert(actualPaths === expectedPaths)
    }

  }
}
