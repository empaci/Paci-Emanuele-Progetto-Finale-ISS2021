package coap

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
 
object actorQakWeightCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8070"		//5683 default
	private val context     = "ctxparkmanager"
 	private val destactor   = "weightsensor"
	private var content     = ""

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				content = response.responseText
                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
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
	
	fun readResponse() : String {
		//var respGet = client.get( );
		//System.out.println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode());		
		//return respGet.getResponseText();
		System.out.println("CoapSupport | readResponse content: " + content);		
		return content;
	}

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
		actorQakWeightCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }