package com.example

import com.example.Freemarker.freemarker
import org.scalatra.util.RequestLogging
import org.scalatra.{ActionResult, Found, Ok, ScalatraServlet}

import java.io.StringWriter
import java.sql.Connection
import scala.jdk.CollectionConverters._

class Vehicles extends ScalatraServlet with RequestLogging {
  implicit val connection: Connection = VehicleDatabase.openConnection()

  private def Template(
      templateName: String,
      model: Map[String, Any]
  ): ActionResult = {
    val template       = freemarker.getTemplate(templateName)
    val responseWriter = new StringWriter()
    template.process(model.asJava, responseWriter)
    Ok(responseWriter.toString)
  }

  get("/") {
    contentType = "text/html"
    val vehicles = VehicleDatabase.listAvailableVehicles().asJava
    Template("main.flth", Map("vehicles" -> vehicles))
  }

  get("/vehicle/:id") {
    val id = params("id").toInt
    contentType = "text/html"
    VehicleDatabase.getVehicle(id) match {
      case Some(vehicle) =>
        Template("vehicle.flth", Map("vehicle" -> vehicle))
      case None =>
        Template("not_found.flth", Map.empty)
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
