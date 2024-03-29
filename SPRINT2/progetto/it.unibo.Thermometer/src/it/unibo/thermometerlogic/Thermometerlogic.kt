/* Generated by AN DISI Unibo */ 
package it.unibo.thermometerlogic

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Thermometerlogic ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Thermometer logic.")
						solve("consult('thermapplogic.pl')","") //set resVar	
						solve("dynamic('criticTemp/1')","") //set resVar	
						solve("init(X)","") //set resVar	
						 val INITSTATUS = getCurSol("X").toString()  
						forward("thermstatusupdate", "thermstatusupdate($INITSTATUS)" ,"thermometer" ) 
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
					}
					 transition(edgeName="t02",targetState="applylogic",cond=whenEvent("local_thermupdate"))
				}	 
				state("applylogic") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("local_thermupdate(W)"), Term.createTerm("local_thermupdate(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val TEMP = "${payloadArg(0)}"  
								forward("thermstatusupdate", "thermstatusupdate($TEMP)" ,"thermometer" ) 
								solve("notice($TEMP,RES)","") //set resVar	
								 val RESULT = getCurSol("RES").toString()  
								if(  RESULT=="above"  
								 ){emit("thermometerevent", "thermometerevent(above)" ) 
								 var W = "warning" + TEMP  
								forward("thermstatusupdate", "thermstatusupdate($W)" ,"thermometer" ) 
								}
								else
								 {if(  RESULT=="below"  
								  ){emit("thermometerevent", "thermometerevent(below)" ) 
								 }
								 }
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
