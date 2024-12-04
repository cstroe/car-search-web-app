package com.example

import org.eclipse.jetty.ee10.webapp.WebAppContext
import org.eclipse.jetty.server.Server
import org.scalatra.servlet.ScalatraListener

object ScalatraMain {
  def main(args: Array[String]): Unit = {
    val port    = 8082
    val server  = new Server(port)
    val context = new WebAppContext()
    context.setContextPath("/")
    context.setBaseResourceAsString("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    server.setHandler(context)

    println(s"Starting Server at http://localhost:$port")
    server.start()
    server.join()
  }
}
