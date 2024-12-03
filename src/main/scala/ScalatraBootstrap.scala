import com.example.Vehicles
import jakarta.servlet.ServletContext
import org.scalatra._

import scala.annotation.unused

@unused("This is used as part of configuring Scalatra at startup")
class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    context.mount(new Vehicles, "/")
  }
}
