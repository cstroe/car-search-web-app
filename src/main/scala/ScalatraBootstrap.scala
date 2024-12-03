import com.example.Vehicles
import jakarta.servlet.ServletContext
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    context.mount(new Vehicles, "/")
  }
}
