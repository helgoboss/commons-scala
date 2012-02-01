package org.helgoboss.commons_scala

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import Implicits._

@RunWith(classOf[JUnitRunner])
class NestedMapSpec extends WordSpec with ShouldMatchers {
  "A nested map" should {
    val m = Map(
      "key1" -> "value1",
      "key2" -> Map(
        "key2.1" -> "value2.1",
        "key2.2" -> Map(
          "key2.2.1" -> "value2.2.1"
        )
      )
    )

    "be able to provide deeply nested values" in {
      assert(m.getDeep("key1") === Some("value1"))
      assert(m.getDeep("key1", "blabla") === None)
      assert(m.getDeep("key2", "key2.2", "key2.2.1") === Some("value2.2.1"))
    }
  }
}
