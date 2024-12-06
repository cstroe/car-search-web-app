package com.example

import java.time.LocalDate

object ExteriorColor {
  def parse(color: String): ExteriorColor = {
    color match {
      case "Obsidian Blue Pearl" =>
        ExteriorColor(color, Option("#2E5293"), Option("B-588P"))
      case "Crystal Black Pearl" =>
        ExteriorColor(color, Option("#000000"), Option("NH731P"))
      case "Lunar Silver Metallic" | "Silver" =>
        ExteriorColor(color, Option("#CCCCCC"), Option("NH-830M"))
      case "Modern Steel Metallic" =>
        ExteriorColor(color, Option("#737373"), Option("NH797M"))
      case "White Diamond Pearl" | "Platinum White Pearl" =>
        ExteriorColor(color, Option("#F9F9F9"), Option("NH603P"))
      case "Radiant Red Metallic II" =>
        ExteriorColor(color, Option("#D32929"), Option("R569M"))
      case "Sonic Gray Pearl" =>
        ExteriorColor(color, Option("#85A3A9"), Option("NH877P"))
      case "Smoke Blue Pearl" =>
        ExteriorColor(color, Option("#0464B9"), Option("B-642P"))
      case _ => ExteriorColor(color)
    }
  }
}

case class ExteriorColor(
    name: String,
    htmlColor: Option[String] = None,
    mfrCode: Option[String] = None
)

case class Vehicle(
    id: Long,
    year: Int,
    make: String,
    model: String,
    trim: String,
    miles: Long,
    price: Long,
    url: String,
    dealer: String,
    vin: String,
    interested: Boolean,
    exterior: ExteriorColor,
    interior: String
)

case class Price(date: LocalDate, amount: Int)
