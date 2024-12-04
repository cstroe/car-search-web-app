package com.example

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
    interested: Boolean
)
