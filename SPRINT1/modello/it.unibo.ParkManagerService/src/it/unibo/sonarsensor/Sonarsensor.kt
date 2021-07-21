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
		   
				var prevDist=0f
				
				fun resourceInfo(dist: Float, preDist: Float) : String {
					if (Math.abs(dist - prevDist) > 3) {
						if (dist.toInt() == 0) {
							return "free"
						}
		 			}
		 			return "occupied"
		 		}
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Starting Sonar sensor.")
						updateResourceRep( "free"  
						)
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("Sonarsensor waiting for data...")
					}
					 transition(edgeName="data22",targetState="handleData",cond=whenDispatch("sonarData"))
				}	 
				state("handleData") { //this:State
					action { //it:State
						println("Sonarsensor received data")
						if( checkMsgContent( Term.createTerm("sonarData(D)"), Term.createTerm("sonarData(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var dist = "${payloadArg(0)}"  
								println("distance: $dist")
								updateResourceRep( resourceInfo(dist.toFloat(), prevDist.toFloat())  
								)
								 prevDist = dist.toFloat()  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}