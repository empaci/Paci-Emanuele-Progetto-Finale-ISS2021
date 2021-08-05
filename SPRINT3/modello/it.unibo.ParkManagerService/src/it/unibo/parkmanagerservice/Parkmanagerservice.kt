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
				var StartTime = 0L
				var DURATION = 0L
				var TOKENID = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						solve("consult('parking.pl')","") //set resVar	
						solve("dynamic('freeSlot/1')","") //set resVar	
						solve("dynamic('occupied/3')","") //set resVar	
						solve("init(X)","") //set resVar	
						StartTime = getCurrentTime()
						coap.actorQakStateCoapObserver.activate(myself)
						println("Starting ParkManagerService.")
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
				state("checkTimeout") { //this:State
					action { //it:State
						DURATION = getDuration(StartTime)
						solve("timedout($DURATION)","") //set resVar	
					}
					 transition( edgeName="goto",targetState="accept", cond=doswitch() )
				}	 
				state("accept") { //this:State
					action { //it:State
						println("ParkManagerServing accepting client requests...")
						stateTimer = TimerActor("timer_accept", 
							scope, context!!, "local_tout_parkmanagerservice_accept", 10000.toLong() )
					}
					 transition(edgeName="clientMsg5",targetState="checkTimeout",cond=whenTimeout("local_tout_parkmanagerservice_accept"))   
					transition(edgeName="clientMsg6",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readWeight()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg7",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg8",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="clientMsg9",targetState="stoptrolley",cond=whenDispatch("stop"))
				}	 
				state("handleClientRequest") { //this:State
					action { //it:State
						println("ParkManagerService handling client request")
						if( checkMsgContent( Term.createTerm("clientRequest(X)"), Term.createTerm("clientRequest(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val requestType = "${payloadArg(0)}"  
								if(  requestType == "in"  
								 ){solve("getFreeSlot(S)","") //set resVar	
								 val SLOTNUM = getCurSol("S")  
								updateResourceRep( "park("+SLOTNUM+")"  
								)
								DURATION = getDuration(StartTime)
								solve("occupySlot($SLOTNUM,0,$DURATION)","") //set resVar	
								answer("clientRequest", "enter", "enter($SLOTNUM)"   )  
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
								solve("isFree($SLOTNUM,X)","") //set resVar	
								 var FREE = getCurSol("X").toString()  
								if(  FREE == "true"  
								 ){solve("indoor(X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								solve("getCoordinates($SLOTNUM,X,Y)","") //set resVar	
								 x = getCurSol("X").toString().toInt()  
								 y = getCurSol("Y").toString().toInt()  
								 forward("move", "move($x,$y)" ,"transporttrolley" )  
								 TOKENID = "$SLOTNUM"+"$counter"  
								 counter++  
								updateResourceRep( "park("+TOKENID+")"  
								)
								solve("assignToken($TOKENID,$SLOTNUM)","") //set resVar	
								}
								else
								 { TOKENID = "0"  
								 }
								answer("carenter", "receipt", "receipt($TOKENID)"   )  
						}
						stateTimer = TimerActor("timer_handleCarEnter", 
							scope, context!!, "local_tout_parkmanagerservice_handleCarEnter", 1000.toLong() )
					}
					 transition(edgeName="t310",targetState="moveTrolleyHome",cond=whenTimeout("local_tout_parkmanagerservice_handleCarEnter"))   
					transition(edgeName="t311",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readWeight()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t312",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t313",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free"  && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t314",targetState="stoptrolley",cond=whenDispatch("stop"))
				}	 
				state("handleClientOut") { //this:State
					action { //it:State
						println("ParkManagerServing out.")
						if(  coap.actorQakStateCoapObserver.readOutdoor()=="free"  
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
								solve("pickup($TOKENID,$SLOTNUM)","") //set resVar	
						}
						}
						stateTimer = TimerActor("timer_handleClientOut", 
							scope, context!!, "local_tout_parkmanagerservice_handleClientOut", 1000.toLong() )
					}
					 transition(edgeName="t415",targetState="moveTrolleyHome",cond=whenTimeout("local_tout_parkmanagerservice_handleClientOut"))   
					transition(edgeName="t416",targetState="handleClientRequest",cond=whenRequestGuarded("clientRequest",{ coap.actorQakStateCoapObserver.readWeight()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t417",targetState="handleCarEnter",cond=whenRequestGuarded("carenter",{ coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t418",targetState="handleClientOut",cond=whenDispatchGuarded("outTokenid",{ coap.actorQakStateCoapObserver.readOutdoor()=="free" && coap.actorQakStateCoapObserver.readTrolley()!="stopped"  
					}))
					transition(edgeName="t419",targetState="stoptrolley",cond=whenDispatch("stop"))
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