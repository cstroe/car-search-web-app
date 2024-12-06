package com.example

import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Using}

object VehicleDatabase {
  def openConnection(): Connection = {
    val jdbcUrl =
      "jdbc:postgresql://localhost:5432/postgres?sslmode=disable&search_path=vehicles"
    val username = "postgres"
    val password = "example"

    // Register the PostgreSQL driver
    Class.forName("org.postgresql.Driver")

    // Connect to the database
    DriverManager.getConnection(jdbcUrl, username, password)
  }

  def listAvailableVehicles()(implicit connection: Connection): Seq[Vehicle] = {
    Using.Manager { use =>
      val st = use(connection.createStatement())
      val result = use(
        st.executeQuery(
          """
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
                      |WHERE available
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

  def getVehicle(id: Int)(implicit connection: Connection): Option[Vehicle] = {
    Using.Manager { use =>
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

  def setUnavailable(id: Int)(implicit connection: Connection): Unit = {
    Using.Manager { use =>
      val st = use(connection.createStatement())
      st.execute(s"""
          |UPDATE vehicles.all
          |SET available = false
          |WHERE id = $id
          |""".stripMargin)
    }.get
  }

  def setInterested(id: Int, interested: Boolean)(implicit
      connection: Connection
  ): Unit = {
    Using.Manager { use =>
      val st = use(connection.createStatement())
      st.execute(s"""
           |UPDATE vehicles.all
           |SET interested = $interested
           |WHERE id = $id
           |""".stripMargin)
    }.get
  }

  def getPrices(vehicleId: Int)(implicit connection: Connection): Seq[Price] = {
    Using.Manager { use =>
      val st = use(connection.createStatement())
      val result = st.executeQuery(s"""
           |SELECT date, price
           |FROM vehicles.price
           |WHERE vehicle = $vehicleId
           |ORDER BY date DESC
           |""".stripMargin)
      val buffer = new ArrayBuffer[Price]()
      while (result.next()) {
        val date = result.getDate(1).toLocalDate
        val price = result.getInt(2)
        buffer.addOne(Price(date, price))
      }
      buffer.toSeq
    }.get
  }
}
