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
import kotlinx.coroutines.Job
import it.unibo.kactor.QakContext
import kotlinx.coroutines.cancelAndJoin
import org.junit.Before

internal class TestUseCase2 {
	
	lateinit var observer : actorQakCoapObserver2
	var job: Job? = null
	
	@Before
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun mainTest() {
		runBlocking{
			launch {
				observer = actorQakCoapObserver2
				observer.activate()
			}
			job = launch {
				QakContext.createContexts("localhost", this, "model.pl", "sysRules.pl")
			}
			delay(6000)
			
			job?.cancelAndJoin()
		}
	}
	
	@Test
	fun testSlotnum() {
		println("============ tslot:=" + observer.tSlot)
		assertTrue(observer.tSlot=="0")
	}
	
} 


object actorQakCoapObserver2 {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8070"      //5683 default
	private val context     = "ctxparkmanager"
 	private val destactor   = "parkmanagerservice"
	
	private var testnum = 0
	public var tSlot = ""

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
					0 -> tSlot = content
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

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
		actorQakCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }
