package com.example

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class VehicleTest extends AnyFunSuite with Matchers {
  test("dealer regex should work") {
    Vehicles.extractDealer(
      "https://www.dealer.com/somepage.html"
    ) shouldBe "dealer"
    Vehicles.extractDealer(
      "https://www.other.com/somepage.html"
    ) shouldBe "other"
    Vehicles.extractDealer("https://main.com/somepage.html") shouldBe "main"
    Vehicles.extractDealer("http://main.com/somepage.html") shouldBe "main"
  }
}
