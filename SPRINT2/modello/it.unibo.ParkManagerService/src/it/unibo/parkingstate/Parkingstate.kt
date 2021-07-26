/* Generated by AN DISI Unibo */ 
package it.unibo.parkingstate

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkingstate ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var sonarobs = coap.actorQakResourceCoapObserver("sonarsensor")
				var weightobs = coap.actorQakResourceCoapObserver("weightsensor")
				var thermometerobs = coap.actorQakResourceCoapObserver("thermometer")
				var trolleyobs = coap.actorQakResourceCoapObserver("transporttrolley")
				var fanobs = coap.actorQakResourceCoapObserver("fan")
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Parkingstate.")
						updateResourceRep( "Empty"  
						)
						sonarobs.activate(myself)
						weightobs.activate(myself)
						thermometerobs.activate(myself)
						trolleyobs.activate(myself)
						fanobs.activate(myself)
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						updateResourceRep( "sonar:" + sonarobs.readContent() + "weight:" + weightobs.readContent() + "thermometer:" + thermometerobs.readContent() + "trolley:" + trolleyobs.readContent() + "fan:" + fanobs.readContent() 
						)
						stateTimer = TimerActor("timer_wait", 
							scope, context!!, "local_tout_parkingstate_wait", 1000.toLong() )
					}
					 transition(edgeName="t034",targetState="wait",cond=whenTimeout("local_tout_parkingstate_wait"))   
				}	 
			}
		}
}
