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
 
class actorQakResourceCoapObserver(destactor : String, port : String, context : String, addr : String) {

    private val client = CoapClient()
	
	private var port 		= "8070"
	private var ipaddr      = "localhost" 
	private var context     = "ctxparkmanager"
 	private var destactor   = "weightsensor"
	private var content     = ""
	private var info		= ""

	init {
		this.destactor = destactor
		this.port = port
		this.context = context
		this.ipaddr = addr
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null){
       val uriStr = "coap://$ipaddr:$port/$context/$destactor"
	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
 				if(  owner!== null ) owner.scope.launch{
 					val event = MsgUtil.buildEvent( "observer","local_resrep","resrep('$content')")								
					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
				}
				
				if (response.responseText.contains("info")) {
					info = response.responseText
				} else {
					content = response.responseText
				}
				
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}
	
	fun readContent() : String {
		System.out.println("CoapSupport $destactor | readResponse content: " + content);
		return content;
	}
	
	fun readInfo() : String {
		return info;
	}

 }