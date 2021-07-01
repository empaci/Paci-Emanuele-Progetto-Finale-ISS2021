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

internal class TestTrolleyPosUC1 {
	
	lateinit var observer : actorQakCoapObserver3
	var job: Job? = null
	
	@Test
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun mainTest() {
		runBlocking{
			launch {
				observer = actorQakCoapObserver3
				observer.activate()
			}
			job = launch {
				QakContext.createContexts("localhost", this, "model.pl", "sysRules.pl")
			}
			delay(8000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK indoorPos:=" + observer.indoorPos)
			assertTrue(observer.indoorPos=="(6, 0)")
			delay(6000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK parkingPos:=" + observer.parkingPos)
			assertTrue(observer.parkingPos=="(1, 1)")
			delay(6000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK homePos:=" + observer.homePos)
			assertTrue(observer.homePos=="(0, 0)")
			delay(6000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK parkingPos:=" + observer.parkingPos)
			assertTrue(observer.parkingPos=="(1, 1)")
			delay(6000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK outdoorPos:=" + observer.outdoorPos)
			assertTrue(observer.outdoorPos=="(6, 4)")
			delay(6000)
			println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK homePos:=" + observer.homePos)
			assertTrue(observer.homePos=="(0, 0)")
			
			job?.cancelAndJoin()
		}
	}
	
} 

object actorQakCoapObserver3 {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8070"      //5683 default
	private val context     = "ctxparkmanager"
 	private val destactor   = "park_manager_service"
	
	private var testnum = 0
	public var indoorPos = ""
	public var parkingPos = ""
	public var outdoorPos = ""
	public var homePos = ""

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
					0 -> println("trolley started")
					1 -> indoorPos = content
					2 -> parkingPos = content
					3 -> homePos = content
					4 -> parkingPos = content
					5 -> outdoorPos = content
					6 -> homePos = content
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
