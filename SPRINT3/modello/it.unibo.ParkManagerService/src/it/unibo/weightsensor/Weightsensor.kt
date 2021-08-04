/* Generated by AN DISI Unibo */ 
package it.unibo.weightsensor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Weightsensor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		   
				var prevWeight=0f
				
				fun resourceInfo(weight: Float, preWeight: Float) : String {
					if (Math.abs(weight - prevWeight) > 10) {
						if (weight.toInt() == 0) {
							return "free"
						}
		 			}
		 			return "occupied"
		 		}
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Weight sensor.")
						updateResourceRep( "free"  
						)
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("Weightsensor waiting for data...")
					}
					 transition(edgeName="data20",targetState="handleData",cond=whenDispatch("weightData"))
				}	 
				state("handleData") { //this:State
					action { //it:State
						println("Weightsensor received data")
						if( checkMsgContent( Term.createTerm("weightData(W)"), Term.createTerm("weightData(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var weight = "${payloadArg(0)}"  
								println("weight: $weight")
								updateResourceRep( resourceInfo(weight.toFloat(), prevWeight.toFloat())  
								)
								 prevWeight = weight.toFloat()  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
