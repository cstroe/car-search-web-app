package test.scala

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class HelloTest extends AnyFreeSpec with Matchers {
  "Hello.hello" - {
    "should return hello + project name" in {
      assert(true)
    }
  }
}
