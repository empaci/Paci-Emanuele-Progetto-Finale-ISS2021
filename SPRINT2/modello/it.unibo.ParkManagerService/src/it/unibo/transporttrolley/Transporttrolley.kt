/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val mapname     = "parkingMap"  		 
				var Myself      = myself   
				var CurrentPlannedMove = "" 
				var x = ""
				var y = ""
				var DIST = ""
				var PREV = "176"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( "$mapname"  )
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						if(  itunibo.planner.plannerUtil.atHome()  
						 ){updateResourceRep( "idle"  
						)
						}
						else
						 {updateResourceRep( "working"  
						 )
						 }
						println("Waiting messages...")
					}
					 transition(edgeName="t020",targetState="handle",cond=whenDispatch("move"))
					transition(edgeName="t021",targetState="stopped",cond=whenDispatch("stop"))
				}	 
				state("handle") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("move(X,Y)"), Term.createTerm("move(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												x = payloadArg(0)
												y = payloadArg(1)
								println("x: $x, y: $y")
								updateResourceRep( "working"  
								)
								itunibo.planner.plannerUtil.planForGoal( x, y  )
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						delay(300) 
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
						println("+++++++++++++++++++++++++++++++ $CurrentPlannedMove")
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({ CurrentPlannedMove.length>0  
					}) )
					transition( edgeName="goto",targetState="parkthecar", cond=doswitchGuarded({! ( CurrentPlannedMove.length>0  
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="wMove", cond=doswitchGuarded({ CurrentPlannedMove == "w"  
					}) )
					transition( edgeName="goto",targetState="turnMove", cond=doswitchGuarded({! ( CurrentPlannedMove == "w"  
					) }) )
				}	 
				state("wMove") { //this:State
					action { //it:State
						request("step", "step(175)" ,"basicrobot" )  
					}
					 transition(edgeName="t022",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t023",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "w"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("turnMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" || CurrentPlannedMove == "r"   
						 ){forward("cmd", "cmd($CurrentPlannedMove)" ,"basicrobot" ) 
						}
					}
					 transition( edgeName="goto",targetState="rotationDone", cond=doswitch() )
				}	 
				state("rotationDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("parkthecar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						if(  ! itunibo.planner.plannerUtil.atPos(x.toInt(),y.toInt())  
						 ){itunibo.planner.plannerUtil.planForGoal( x, y  )
						}
						else
						 { println(itunibo.planner.plannerUtil.get_curPos().toString())  
						 if(  itunibo.planner.plannerUtil.atHome()  
						  ){updateResourceRep( "idle"  
						 )
						 }
						 }
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ ! itunibo.planner.plannerUtil.atPos(x.toInt(),y.toInt())  
					}) )
					transition( edgeName="goto",targetState="checkStop", cond=doswitchGuarded({! ( ! itunibo.planner.plannerUtil.atPos(x.toInt(),y.toInt())  
					) }) )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,OBSTACLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												DIST = payloadArg(0) //sostituire con pathexecutil.waitUser("Reposition the robot",10000)
								request("step", "step($DIST)" ,"basicrobot" )  
						}
					}
					 transition(edgeName="t124",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t125",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("checkStop") { //this:State
					action { //it:State
						stateTimer = TimerActor("timer_checkStop", 
							scope, context!!, "local_tout_transporttrolley_checkStop", 200.toLong() )
					}
					 transition(edgeName="t126",targetState="wait",cond=whenTimeout("local_tout_transporttrolley_checkStop"))   
					transition(edgeName="t127",targetState="stopped",cond=whenDispatch("stop"))
				}	 
				state("stopped") { //this:State
					action { //it:State
						println("STOPPED !!!!!!!!!!!!!!!!!!!!!!! STOPPED !!!!!!!!!!!!!!!!!!!! STOPPED !!!!!!!!!!!!!!")
						updateResourceRep( "stopped"  
						)
					}
					 transition(edgeName="t228",targetState="wait",cond=whenDispatch("start"))
				}	 
			}
		}
}
