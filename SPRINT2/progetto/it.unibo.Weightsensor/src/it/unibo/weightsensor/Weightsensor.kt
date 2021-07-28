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
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Weight sensor.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("Weightsensor waiting for data...")
					}
					 transition(edgeName="t00",targetState="handleData",cond=whenDispatch("weightData"))
					transition(edgeName="t01",targetState="updateStatus",cond=whenDispatch("weightstatusupdate"))
				}	 
				state("handleData") { //this:State
					action { //it:State
						println("Weightsensor received data")
						if( checkMsgContent( Term.createTerm("weightData(W)"), Term.createTerm("weightData(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var WEIGHT = "${payloadArg(0)}"  
								emit("localweightupdate", "localweightupdate($WEIGHT)" ) 
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("updateStatus") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("weightstatusupdate(S)"), Term.createTerm("weightstatusupdate(S)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var status = "${payloadArg(0)}"  
								updateResourceRep( status  
								)
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
