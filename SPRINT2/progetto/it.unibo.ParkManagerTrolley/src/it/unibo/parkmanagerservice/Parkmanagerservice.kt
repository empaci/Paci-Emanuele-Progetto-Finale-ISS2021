/* Generated by AN DISI Unibo */ 
package it.unibo.parkmanagerservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkmanagerservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var counter=0 
				var x = 0
				var y = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						solve("consult('parking.pl')","") //set resVar	
						solve("dynamic('freeSlot/1')","") //set resVar	
						solve("unoccupySlot(1)","") //set resVar	
						solve("unoccupySlot(2)","") //set resVar	
						solve("unoccupySlot(3)","") //set resVar	
						solve("unoccupySlot(4)","") //set resVar	
						solve("unoccupySlot(5)","") //set resVar	
						solve("unoccupySlot(6)","") //set resVar	
						coap.actorQakStateCoapObserver.activate(myself)
						println("Starting ParkManagerService.")
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
				state("accept") { //this:State
					action { //it:State
						println("ParkManagerServing accepting client requests...")
					}
					 transition(edgeName="clientMsg0",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg1",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg2",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg3",targetState="stoptrolley",cond=whenDispatch("stop"))
				}	 
				state("handleClientRequest") { //this:State
					action { //it:State
						println("ParkManagerService handling client request")
						if( checkMsgContent( Term.createTerm("clientRequest(X)"), Term.createTerm("clientRequest(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val requestType = "${payloadArg(0)}"  
								if(  requestType == "in"  
								 ){if(  coap.actorQakStateCoapObserver.readWeight()=="free"  
								 ){solve("getFreeSlot(S)","") //set resVar	
								 val SLOTNUM = getCurSol("S")  
								updateResourceRep( "enter("+SLOTNUM+")"  
								)
								solve("occupySlot($SLOTNUM)","") //set resVar	
								answer("clientRequest", "enter", "enter($SLOTNUM)"   )  
								}
								else
								 {answer("clientRequest", "enter", "enter(0)"   )  
								 }
								}
						}
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
				state("handleCarEnter") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("carenter(SLOTNUM)"), Term.createTerm("carenter(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val SLOTNUM = payloadArg(0).toInt()  
								solve("indoor(X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								solve("getCoordinates($SLOTNUM,X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								 val TOKENID = "$SLOTNUM"+"$counter"  
								 counter++  
								updateResourceRep( "receipt("+TOKENID+")"  
								)
								answer("carenter", "receipt", "receipt($TOKENID)"   )  
						}
						stateTimer = TimerActor("timer_handleCarEnter", 
							scope, context!!, "local_tout_parkmanagerservice_handleCarEnter", 1000.toLong() )
					}
					 transition(edgeName="t34",targetState="moveTrolleyHome",cond=whenTimeout("local_tout_parkmanagerservice_handleCarEnter"))   
					transition(edgeName="t35",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t36",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t37",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free"  && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t38",targetState="stoptrolley",cond=whenDispatch("stop"))
				}	 
				state("handleClientOut") { //this:State
					action { //it:State
						println("ParkManagerServing out.")
						if(  coap.actorQakStateCoapObserver.readResponse()=="free"  
						 ){if( checkMsgContent( Term.createTerm("outTokenid(TOKENID)"), Term.createTerm("outTokenid(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val TOKENID = "${payloadArg(0)}"  
								 val SLOTNUM =  TOKENID.first()  
								solve("getCoordinates($SLOTNUM,X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								solve("outdoor(X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								solve("unoccupySlot($SLOTNUM)","") //set resVar	
						}
						}
						stateTimer = TimerActor("timer_handleClientOut", 
							scope, context!!, "local_tout_parkmanagerservice_handleClientOut", 1000.toLong() )
					}
					 transition(edgeName="t49",targetState="moveTrolleyHome",cond=whenTimeout("local_tout_parkmanagerservice_handleClientOut"))   
					transition(edgeName="t410",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t411",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t412",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t413",targetState="stoptrolley",cond=whenDispatch("stop"))
				}	 
				state("moveTrolleyHome") { //this:State
					action { //it:State
						solve("home(X,Y)","") //set resVar	
						 x = getCurSol("X").toString().toInt()  
						 y = getCurSol("Y").toString().toInt()  
						 forward("move", "move($x,$y)" ,"transporttrolley" )  
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
				state("stoptrolley") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("stop(X)"), Term.createTerm("stop(CMD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val CMD = "${payloadArg(0)}"  
								if(  CMD == "stop"  
								 ){forward("stop", "stop($CMD)" ,"transporttrolley" ) 
								}
								else
								 {if(  CMD == "start"  
								  ){forward("start", "start($CMD)" ,"transporttrolley" ) 
								 }
								 }
						}
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
			}
		}
}