package caffeinepof

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalacache._
import scalacache.caffeine._

import scala.concurrent.Future

class CaffeineTest extends AnyFlatSpec with Matchers with ScalaFutures  {
  "The sync mode" should "return a value wrapped into a Option[T]" in {

    import scalacache.modes.sync._
    final case class Cat(id: Int, name: String, colour: String)
    val myCat = Cat(0, "felix", "brown")

    implicit val caffeineCache: Cache[Cat] = CaffeineCache[Cat]
    put("felix")(myCat)
    get("felix") shouldBe Some(myCat)

  }

  "The async mode" should "return a value wrapped into a Future[T]" in {

    import scalacache.modes.scalaFuture._
    import scala.concurrent.ExecutionContext.Implicits.global
    final case class Cat(id: Int, name: String, colour: String)
    val myCat = Cat(0, "felix", "brown")

    def getValueFromDB(key: String): Future[Cat] = ???

    getValueFromDB("felix").map(put("felix", _))

    implicit val caffeineCache: Cache[Cat] = CaffeineCache[Cat]
    put("felix")(myCat).futureValue
    get("felix").futureValue shouldBe Some(myCat)

  }
}
