package org.helgoboss.commons_scala

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import MatchResult._

@RunWith(classOf[JUnitRunner])
class PathPatternSpec extends WordSpec with ShouldMatchers {
  "A path pattern with single stars when matching against a path" should {
    val p = PathPattern("dir1/*/dir3/*file1*.txt")

    "return " + NeverMatches + " if at least one component in the path doesn't match" in {
      assert((p matchAgainst "dir/test1/dir3/file1.txt") === NeverMatches)
      assert((p matchAgainst "dir") === NeverMatches)
    }

    "return " + CanMatchSubPath + " if the path is shorter but at least matches until its end" in {
      assert((p matchAgainst "dir1/test") === CanMatchSubPath)
    }

    "return " + Matches + " if all components of the path match" in {
      assert((p matchAgainst "dir1/test1/dir3/file1.txt") === Matches)
      assert((p matchAgainst "dir1/test1/dir3/my-file1-here.txt") === Matches)
    }
  }

  "A path pattern with double stars when matching against a path" should {
    val p = PathPattern("dir1/**/dir2/**/file1.txt")

    "return " + Matches + " even if there's no directory in the path corresponding to **" in {
      assert((p matchAgainst "dir1/dir2/file1.txt") === Matches)
    }

    "return " + Matches + " if there's a single directory in the path corresponding to **" in {
      assert((p matchAgainst "dir1/test1/dir2/test2/file1.txt") === Matches)
    }

    "return " + CanMatchSubPath + " if the path is shorter but at least matches until its end" in {
      assert((p matchAgainst "dir1") === CanMatchSubPath)
      assert((p matchAgainst "dir1/test1") === CanMatchSubPath)
      assert((p matchAgainst "dir1/test1/moin") === CanMatchSubPath)
      assert((p matchAgainst "dir1/test1/moin/dir2") === CanMatchSubPath)
      assert((p matchAgainst "dir1/test1/moin/dir2/test2") === CanMatchSubPath)
    }

    "return " + Matches + " if there are multiple directories in the path corresponding to **" in {
      assert((p matchAgainst "dir1/test1/dir2/test2/dir3/file1.txt") === Matches)
    }

    "match the double stars in a greedy manner" in {
      assert((p matchAgainst "dir1/dir2/file1.txt/file1.txt") === Matches)
    }
  }
  "The popular path pattern **/* when matching against a path" should {
    val p = PathPattern("**/*")

    "match the double stars in a greedy manner" in {
      assert((p matchAgainst "a/b/c") === Matches)
    }
  }

  "A path pattern with double stars at the end when matching against a path" should {
    val p = PathPattern("dir1/**/dir2/**")

    "return " + Matches + " if the path matches until the pattern component before the last ** and goes on" in {
      assert((p matchAgainst "dir1/test1/dir2/test2/dir3/file1.txt") === Matches)
      assert((p matchAgainst "dir1/test1/dir2/file1.txt") === Matches)
      assert((p matchAgainst "dir1/test1/dir2/dir3") === Matches)
    }

    "return " + WillMatchEverySubPath + " if the complete path matches until the pattern component before the last **" in {
      assert((p matchAgainst "dir1/test1/dir2") === WillMatchEverySubPath)
    }

  }
}
