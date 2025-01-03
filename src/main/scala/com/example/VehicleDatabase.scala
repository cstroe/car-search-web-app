package com.example

import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Using}

object VehicleDatabase {
  def listAvailableVehicles(onlyFavorites: Boolean = false): Seq[Vehicle] = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      val favoritesClause = if (onlyFavorites) {
        Option("AND interested = true")
      } else { None }
      val result = use(
        st.executeQuery(
          s"""
                      |SELECT
                      |  id, year, make, model, trim, miles, url,
                      |  (
                      |    SELECT price
                      |    FROM vehicles.price
                      |    WHERE vehicle = v.id
                      |    ORDER BY date DESC
                      |    LIMIT 1
                      |  ),
                      |  dealer,
                      |  vin,
                      |  interested, exterior, interior
                      |FROM vehicles.all AS v
                      |WHERE available ${favoritesClause.getOrElse("")}
                      |ORDER BY year, make, model, trim;
          |""".stripMargin
        )
      )
      val buffer = new ArrayBuffer[Vehicle]()
      while (result.next()) {
        buffer.addOne(parseVehicle(result).get)
      }
      buffer.toSeq
    }.get
  }

  private def parseVehicle(result: ResultSet): Try[Vehicle] = Try {
    val id = result.getLong(1)
    val year = result.getInt(2)
    val make = result.getString(3)
    val model = result.getString(4)
    val trim = result.getString(5)
    val miles = result.getInt(6)
    val url = result.getString(7)
    val price = result.getInt(8)
    val dealer = result.getString(9)
    val vin = result.getString(10)
    val interested = result.getBoolean(11)
    val exterior = ExteriorColor.parse(result.getString(12))
    val interior = result.getString(13)

    Vehicle(
      id,
      year,
      make,
      model,
      trim,
      miles,
      price,
      url,
      dealer,
      vin,
      interested,
      exterior,
      interior
    )
  }

  def getVehicle(id: Int): Option[Vehicle] = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      val result = use(
        st.executeQuery(
          s"""
            |SELECT
            |  id, year, make, model, trim, miles, url,
            |  (
            |    SELECT price
            |    FROM vehicles.price
            |    WHERE vehicle = v.id
            |    ORDER BY date DESC
            |    LIMIT 1
            |  ),
            |  dealer, vin, interested, exterior, interior
            |FROM vehicles.all AS v
            |WHERE id = $id;
            |""".stripMargin
        )
      )

      if (!result.next()) { Option.empty }
      else { parseVehicle(result).toOption }
    }.get
  }

  def setUnavailable(id: Int): Unit = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      st.execute(s"""
          |UPDATE vehicles.all
          |SET available = false
          |WHERE id = $id
          |""".stripMargin)
    }.get
  }

  def setInterested(id: Int, interested: Boolean): Unit = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      st.execute(s"""
           |UPDATE vehicles.all
           |SET interested = $interested
           |WHERE id = $id
           |""".stripMargin)
    }.get
  }

  def getPrices(vehicleId: Int): Seq[Price] = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      val result = st.executeQuery(s"""
           |SELECT date, price, id
           |FROM vehicles.price
           |WHERE vehicle = $vehicleId
           |ORDER BY date DESC
           |""".stripMargin)
      val buffer = new ArrayBuffer[Price]()
      while (result.next()) {
        val date = result.getDate(1).toLocalDate
        val price = result.getInt(2)
        val id = result.getInt(3)
        buffer.addOne(Price(id, date, price))
      }
      buffer.toSeq
    }.get
  }

  def insertVehicle(vehicle: Vehicle): Unit = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      val resultSet = st.executeQuery(s"""
          |INSERT INTO vehicles.all (
          |  year, make, model, trim, exterior, interior, miles,
          |  vin, interested, dealer, url)
          |VALUES (
          |  ${vehicle.year},
          |  '${vehicle.make}',
          |  '${vehicle.model}',
          |  '${vehicle.trim}',
          |  '${vehicle.exterior.name}',
          |  '${vehicle.interior}',
          |  ${vehicle.miles},
          |  '${vehicle.vin}',
          |  false,
          |  '${vehicle.dealer}',
          |  '${vehicle.url}'
          |) RETURNING id;""".stripMargin)
      resultSet.next()
      val newId = resultSet.getInt(1)
      st.execute(
        s"""
           |INSERT INTO vehicles.price (vehicle, date, price)
           |VALUES ($newId, CURRENT_DATE, ${vehicle.price});
           |""".stripMargin
      )
    }.get
  }

  def insertPrice(vehicleId: Int, price: Int): Unit = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val st = use(connection.createStatement())
      st.execute(
        s"""
           |INSERT INTO vehicles.price (vehicle, date, price)
           |VALUES (
           |  $vehicleId,
           |  CURRENT_DATE,
           |  $price);""".stripMargin
      )
    }.get
  }

  def deletePrice(vehicleId: Int, priceId: Int): Unit = {
    Using.Manager { use =>
      val connection = use(DataSource.dataSource.getConnection)
      val preparedSt = connection.prepareStatement(
        s"""
           |DELETE FROM vehicles.price
           |WHERE vehicle = ? AND id = ?;""".stripMargin
      )
      preparedSt.setInt(1, vehicleId)
      preparedSt.setInt(2, priceId)
      preparedSt.execute()
    }.get
  }
}
