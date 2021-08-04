/* Generated by AN DISI Unibo */ 
package it.unibo.sonarsensor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarsensor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Sonar sensor.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("Sonarsensor waiting for data...")
					}
					 transition(edgeName="t00",targetState="handleData",cond=whenDispatch("sonarData"))
					transition(edgeName="t01",targetState="updateStatus",cond=whenDispatch("sonarstatusupdate"))
				}	 
				state("handleData") { //this:State
					action { //it:State
						println("Weightsensor received data")
						if( checkMsgContent( Term.createTerm("sonarData(D)"), Term.createTerm("sonarData(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var DISTANCE = "${payloadArg(0)}"  
								emit("local_sonarupdate", "local_sonarupdate($DISTANCE)" ) 
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("updateStatus") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sonarstatusupdate(S)"), Term.createTerm("sonarstatusupdate(S)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var status = "${payloadArg(0)}"  
								updateResourceRep( status  
								)
								println("$status")
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
