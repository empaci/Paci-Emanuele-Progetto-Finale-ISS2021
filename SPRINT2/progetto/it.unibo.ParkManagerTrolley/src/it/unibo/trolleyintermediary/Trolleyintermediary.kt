/* Generated by AN DISI Unibo */ 
package it.unibo.trolleyintermediary

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolleyintermediary ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
					}
					 transition(edgeName="t020",targetState="handleEvent",cond=whenEvent("localeventbasic"))
				}	 
				state("handleEvent") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("localeventbasic(CMD)"), Term.createTerm("localeventbasic(CMD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												var CMD = payloadArg(0)
								if(  CMD == "w"  
								 ){request("step", "step(175)" ,"basicrobot" )  
								}
								else
								 {forward("cmd", "cmd($CMD)" ,"basicrobot" ) 
								 }
						}
					}
					 transition(edgeName="t021",targetState="wait",cond=whenReply("stepdone"))
					transition(edgeName="t022",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,OBSTACLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												var DIST = payloadArg(0) //sostituire con pathexecutil.waitUser("Reposition the robot",10000)
								request("step", "step($DIST)" ,"basicrobot" )  
						}
					}
					 transition(edgeName="t123",targetState="wait",cond=whenReply("stepdone"))
					transition(edgeName="t124",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
			}
		}
}
