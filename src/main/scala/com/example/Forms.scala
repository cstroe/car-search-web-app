package com.example
import org.scalatra.forms._

case class NewVehicleForm(
    year: Int,
    make: String,
    model: String,
    trim: String,
    exterior: String,
    interior: String,
    miles: Int,
    price: Int,
    vin: String,
    url: String
)
