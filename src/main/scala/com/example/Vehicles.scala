package com.example

import com.example.Templates.html.{
  main,
  newVehicle => newVehicleTemplate,
  notFound => notFoundTemplate,
  vehicle => vehicleTemplate
}
import com.example.Vehicles.{extractDealer, newPriceForm, newVehicleForm}
import org.scalatra.forms._
import org.scalatra.i18n.I18nSupport
import org.scalatra.util.RequestLogging
import org.scalatra.{ActionResult, BadRequest, Found, Ok, ScalatraServlet}

import scala.util.matching.Regex

object Vehicles {
  private val URL_REGEX: Regex = "https?://(www\\.)?([a-z]+)\\.com.*".r

  def extractDealer(url: String): String = {
    URL_REGEX.findFirstMatchIn(url) match {
      case Some(patternMatch) => patternMatch.group(2)
      case None               => ""
    }
  }

  val newVehicleForm: MappingValueType[NewVehicleForm] = mapping(
    "year" -> label("Year", number(required)),
    "make" -> label("Make", text(required)),
    "model" -> label("Model", text(required)),
    "trim" -> label("Trim", text(required)),
    "exterior" -> label("Exterior", text(required)),
    "interior" -> label("Interior", text(required)),
    "miles" -> label("Miles", number(required)),
    "price" -> label("Price", number(required)),
    "vin" -> label("VIN", text(required)),
    "url" -> label("URL", text(required))
  )(NewVehicleForm.apply)

  val newPriceForm: MappingValueType[NewPriceForm] = mapping(
    "price" -> label("Price", number(required))
  )(NewPriceForm.apply)
}

class Vehicles
    extends ScalatraServlet
    with RequestLogging
    with FormSupport
    with I18nSupport {
  private val URL_REGEX: Regex = "https?://(www.)?(.*)+\\.(org|com|net])/.*".r

  get("/") {
    contentType = "text/html"
    val vehicles = VehicleDatabase.listAvailableVehicles()
    Ok(main.apply(vehicles).toString())
  }

  private def singleVehicle(id: Int): ActionResult = {
    VehicleDatabase.getVehicle(id) match {
      case Some(vehicle) =>
        Ok(vehicleTemplate(vehicle, VehicleDatabase.getPrices(id)))
      case None =>
        Ok(notFoundTemplate())
    }
  }

  get("/vehicle/:id") {
    val id = params("id").toInt
    contentType = "text/html"
    singleVehicle(id)
  }

  post("/vehicle/:id/new_price") {
    val id = params("id").toInt
    contentType = "text/html"
    validate[NewPriceForm, ActionResult](newPriceForm)(
      hasErrors = _ => {
        singleVehicle(id)
      },
      success = form => {
        VehicleDatabase.insertPrice(id, form.price)
        Found(s"/vehicle/$id")
      }
    )
  }

  // TODO: This should be a post action
  get("/vehicle/:vehicle_id/delete_price/:price_id") {
    val vehicleId = params("vehicle_id").toInt
    val priceId = params("price_id").toInt
    VehicleDatabase.deletePrice(vehicleId, priceId)
    Found(s"/vehicle/$vehicleId")
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

  get("/new_vehicle") {
    Ok(newVehicleTemplate())
  }

  post("/new_vehicle") {
    contentType = "text/html"
    validate[NewVehicleForm, ActionResult](newVehicleForm)(
      hasErrors = _ => {
        BadRequest(newVehicleTemplate())
      },
      success = form => {
        VehicleDatabase.insertVehicle(
          Vehicle(
            id = 0,
            year = form.year,
            make = form.make,
            model = form.model,
            trim = form.trim,
            miles = form.miles,
            price = form.price,
            url = form.url,
            dealer = extractDealer(form.url),
            vin = form.vin,
            interested = false,
            exterior = ExteriorColor(name = form.exterior),
            interior = form.interior
          )
        )
        Found("/")
      }
    )
  }
}
