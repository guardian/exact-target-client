import com.gu.email.exacttarget.{ExactTargetSoapApiService, ExactTargetFactory}
import com.gu.email.{Subscriber, AccountDetails, ListSubscriber}
import com.gu.email.xml.XmlRequestSender
import java.net.URI
import org.apache.commons.httpclient.HttpClient
import org.scalatest.FlatSpec
import org.scalatest.Matchers


class ListSubscriberHarness extends FlatSpec with Matchers {

        val subscribers = List(Subscriber("natbennett@hotmail.com", Some("Nat"), Some("Bennett"), status=Some("Active") ) )

       "this" should "add a subcriber to a list" in {
          val factory = new ExactTargetFactory("gnmtestuser", "row_4boat", new URI("https://webservice.s4.exacttarget.com/Service.asmx"))
          val httpClient = new HttpClient()
          val service = new TestSoapFactory(factory, httpClient)
          service.subscribeToList("alert_profile%2Fcharlesarthur", Some("1310199"), subscribers )
          //service.subscribeToList("1673", Some("1310199"), subscribers )
          //service.subscribeToList("alert_uk%2Fuk", Some("1062022"), subscribers)
       }

}

class TestSoapFactory(factory: ExactTargetFactory, client : HttpClient ) extends ExactTargetSoapApiService(factory, client) with ListSubscriber {
  override val xmlRequestSender = new XmlRequestSender(client)
  override val accountDetails = new AccountDetails("gnmtestuser", "row_4boat")
}




