package com.example

case class Field(name: String, hideTitle: Boolean = false)

object Vehicle {
  private val fields = List(
    Field("id"),
    Field("interested", hideTitle = true),
    Field("dealer"),
    Field("year"),
    Field("make"),
    Field("model"),
    Field("trim"),
    Field("miles"),
    Field("price"),
    Field("vin"),
    Field("url"),
    Field("action"),
  )

  val header: String = {
    Vehicle.fields
      .map(i => if (i.hideTitle) { "  <th></th>" } else { "  <th>" + i.name + "</th>" })
      .mkString("<tr>\n", "\n", "<tr>\n")
  }
}

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
                  ) {
  private val formatter = java.text.NumberFormat.getIntegerInstance

  def toHtml: String = {
    Vehicle.fields.map(_.name).map {
      case "id" => id
      case "dealer" => dealer
      case "year" => year
      case "make" => make
      case "model" => model
      case "trim" => trim
      case "miles" => formatter.format(miles            )
      case "vin" => vin
      case "price" => "$" + formatter.format(price)
      case "url" => s"""<a href="$url" target="_blank">LINK</a>"""
      case "action" => s"""<a href="/set_as_unavailable/$id">SET UNAVAILABLE</a>"""
      case "interested" =>
        if (interested) { s"""<a href="/mark_not_interested/$id">\uD83C\uDF1F</a>""" }
        else { s"""<a href="/mark_interested/$id">➕</a>"""          }
    }
      .map(i => "<td>" + i + "</td>")
      .mkString("<tr>\n", "\n", "</tr>\n")
  }
}
