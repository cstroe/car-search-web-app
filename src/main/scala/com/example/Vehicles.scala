package com.example

import com.example.Templates.html.{
  main,
  vehicle => vehicleTemplate,
  notFound => notFoundTemplate
}
import org.scalatra.util.RequestLogging
import org.scalatra.{Found, Ok, ScalatraServlet}

import java.sql.Connection

class Vehicles extends ScalatraServlet with RequestLogging {
  implicit val connection: Connection = VehicleDatabase.openConnection()

  get("/") {
    contentType = "text/html"
    val vehicles = VehicleDatabase.listAvailableVehicles()
    Ok(main.apply(vehicles).toString())
  }

  get("/vehicle/:id") {
    val id = params("id").toInt
    contentType = "text/html"
    VehicleDatabase.getVehicle(id) match {
      case Some(vehicle) =>
        Ok(vehicleTemplate(vehicle, VehicleDatabase.getPrices(id)))
      case None =>
        Ok(notFoundTemplate())
    }
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
