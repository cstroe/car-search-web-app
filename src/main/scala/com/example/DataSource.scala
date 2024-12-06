package com.example

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

// Followed example from https://www.baeldung.com/hikaricp
object DataSource {
  val dataSource: HikariDataSource = {
    val config = new HikariConfig()
    config.setJdbcUrl(
      "jdbc:postgresql://localhost:5432/postgres?sslmode=disable&search_path=vehicles"
    )
    config.setUsername("postgres")
    config.setPassword("example")
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    new HikariDataSource(config)
  }
}
