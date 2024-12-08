package com.example

import java.time.LocalDate

object ExteriorColor {
  def parse(color: String): ExteriorColor = {
    // Source: https://www.hondainfocenter.com/2022/Odyssey/Feature-Guide/Colors/
    // Source: https://www.hondainfocenter.com/2020/Odyssey/Feature-Guide/Colors-Trim-Guide/
    color match {
      case "Obsidian Blue Pearl" | "Blue" =>
        ExteriorColor(color, Option("#2E5293"), Option("B-588P"))
      case "Crystal Black Pearl" | "Black" =>
        ExteriorColor(color, Option("#000000"), Option("NH-731P"))
      case "Lunar Silver Metallic" | "Silver" =>
        ExteriorColor(color, Option("#CCCCCC"), Option("NH-830M"))
      case "Modern Steel Metallic" =>
        ExteriorColor(color, Option("#737373"), Option("NH-797M"))
      case "White Diamond Pearl" =>
        ExteriorColor(color, Option("#F9F9F9"), Option("NH603P"))
      case "Radiant Red Metallic II" =>
        ExteriorColor(color, Option("#D32929"), Option("R-580M"))
      case "Sonic Gray Pearl" =>
        ExteriorColor(color, Option("#85A3A9"), Option("NH877P"))
      case "Smoke Blue Pearl" =>
        ExteriorColor(color, Option("#0464B9"), Option("B-642P"))
      case "Pacific Pewter Metallic" =>
        ExteriorColor(color, Option("#6F684D"), Option("NH-862M"))
      case "Deep Scarlet Pearl" =>
        ExteriorColor(color, Option("#6A2F2F"), Option("R-561P"))
      case "Platinum White Pearl" =>
        ExteriorColor(color, Option("#F9F9F9"), Option("NH-883P"))
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

case class Price(id: Int, date: LocalDate, amount: Int)
