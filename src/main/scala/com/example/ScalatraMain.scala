package com.example

import org.eclipse.jetty.ee10.webapp.WebAppContext
import org.eclipse.jetty.server.Server
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

object ScalatraMain {
  private val logger = LoggerFactory.getLogger(ScalatraMain.getClass)

  def main(args: Array[String]): Unit = {
    val port = 8082
    val server = new Server(port)
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setBaseResourceAsString("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    server.setHandler(context)

    logger.info(s"Starting Server at http://localhost:$port")
    server.start()
    server.join()
  }
}
