import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 
import org.junit.Test
import org.junit.Assert.assertTrue


internal class TestUseCase1 {

    @Test
    fun testSum() {
        val expected = 42
        assertTrue(expected==42)
    }
}
 
object actorQakCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8070"      //5683 default
	private val context     = "ctxparkmanager"
 	private val destactor   = "park_manager_service"
	
	private var testnum = 0

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
				
				when (testnum) {
					0 -> testSlot(content)
					1 -> testTokenid(content)
				}
				testnum++
				
 				if(  owner!== null ) owner.scope.launch{
 					val event = MsgUtil.buildEvent( "observer","local_resrep","resrep('$content')")								
					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
				}
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}

 }

@Test
fun testSlot(slotnum: String) {
	assertTrue(slotnum.toInt()==1)
}

@Test
fun testTokenid(tokenid: String) {
	assertTrue(tokenid=="11")
}

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
		actorQakCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }
