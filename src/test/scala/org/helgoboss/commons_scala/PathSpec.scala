package org.helgoboss.commons_scala

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import MatchResult._

@RunWith(classOf[JUnitRunner])
class PathSpec extends WordSpec with ShouldMatchers {
  "A path" should {
    val p = Path("""/C:/dir1/dir2/../dir2/./dir3/""")
    val pp = PathPattern("C:/dir1/**")

    "should return the correct path components" in {
      assert(p.components === List("C:", "dir1", "dir2", "dir3"))
    }

    "should return the correct normalized path expression" in {
      assert(p.toString === "C:/dir1/dir2/dir3")
    }

    "should be prependable to another path" in {
      val p2 = Path("dir4/dir5")
      assert(p / p2 === Path("C:/dir1/dir2/dir3/dir4/dir5"))
    }

    "match a PathPattern if its matchAgainst method returns " + Matches in {
      assert(p matches pp)
    }

    "match a PathPattern if its matchAgainst method returns " + WillMatchEverySubPath in {
      val p2 = Path("C:/dir1")
      assert(p2 matches pp)
    }
  }
}
