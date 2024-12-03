package com.example

import org.scalatra.util.RequestLogging
import org.scalatra.{Found, Ok, ScalatraServlet}

import java.sql.Connection

class Vehicles extends ScalatraServlet with RequestLogging {
  implicit val connection: Connection = VehicleDatabase.openConnection()

  get("/") {
    contentType = "text/html"
    val vehicles = VehicleDatabase.listAvailableVehicles()
    val response = {
      "<html>" +
        "<head><style>" +
        "td {" +
        "  padding: 10px;" +
        "}" +
        "table {" +
        "  border: 1px solid black;" +
        "}" +
        "</style></head>" +
        "<body>" +
        "<h1>Vehicles</h1><table>" +
        Vehicle.header +
        vehicles.map(_.toHtml).mkString("\n") + "</table>" +
        "</body></html>"
    }
    Ok(response)
  }

  get("/set_as_unavailable/:id") {
    val id = params("id").toInt
    VehicleDatabase.setUnavailable(id)
    Found("/")
  }

  get("/mark_not_interested/:id") {
    val id = params("id").toInt
    VehicleDatabase.setInterested(id, interested = false)
    Found("/")
  }

  get("/mark_interested/:id") {
    val id = params("id").toInt
    VehicleDatabase.setInterested(id, interested = true)
    Found("/")
  }
}
