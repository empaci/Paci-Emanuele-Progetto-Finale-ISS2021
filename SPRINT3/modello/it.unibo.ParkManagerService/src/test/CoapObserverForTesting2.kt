package test.kotlin

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.coap.CoAP

class CoapObserverForTesting2(val name: String      = "testingobs2",
							 val context: String   = "ctxparkmanager",
							 val observed : String = "weightsensor",
							 val port: String      = "8070") {
   //private val client  = CoapClient()
   private var handler : CoapHandler? = null
   private var client : CoapClient?  = null
	
   fun setup(){
 	   client     = CoapClient()
	   val uriStr = "coap://localhost:$port/$context/$observed"   
	   println("	%%%%%% $name | START uriStr: $uriStr"  )
       client!!.uri = uriStr	   
   }
	
   fun addObserver(  channel : Channel<String>, expected:String?=null ){
	   /*
	   val uriStr = "coap://localhost:8020/ctxbasicrobot/$observed"   
	   println("	%%%%%% $name | START uriStr: $uriStr expected=$expected")
       client.uri = uriStr*/
	   setup()
       client!!.observe( object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("	%%%%%% $name | content=$content  expected=$expected RESP-CODE=${response.code} " )
				println("OOIOIOIOI content : $content")
				/*
                    2.05 means content (like HTTP 200 "OK" but only used in response to GET requests)
 					4.04 means NOT FOUND
				*/
				if( response.code == CoAP.ResponseCode.NOT_FOUND ) return
				//DISCARD the content not related to testing
				if( content.contains("START") || content.contains("created")) return
				if( expected != null) {
					runBlocking {
						channel.send(content)
					}
				} else {
						println("content=$content expected=$expected")
				}
			}
            override fun onError() {
                println("$name | FAILED")
            }
        })		
	}		 

   fun removeObserver(  ){
		client = null
 	    println("	%%%%%%  CoapObserverForTesting | TERMINATE")
  }	

		
}