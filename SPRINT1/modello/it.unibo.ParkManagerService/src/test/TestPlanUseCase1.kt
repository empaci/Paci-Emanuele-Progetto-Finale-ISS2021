package test.kotlin

import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import org.junit.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import org.junit.After
import test.kotlin.CoapObserverForTesting
 
 
class TestPlanUseCase1 {
		
	companion object{
		var systemStarted         = false
		var N                     = 6 //N from 1 to 6
		val channelSyncStart      = Channel<String>()
		var testingObserver1       : CoapObserverForTesting? = null
		var testingObserver2       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null

		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxparkmanager.main() //keep the control
			}
			GlobalScope.launch{
				myactor=QakContext.getActor("parkmanagerservice")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkmanagerservice")
				}				
				//delay(1000)	//Give time to set up
				channelSyncStart.send("starttesting")
				testingObserver1 = CoapObserverForTesting()
				testingObserver2 = CoapObserverForTesting()
			}		 
		}//init
		
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object
	
	@Before
	fun checkSystemStarted()  {
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		}
		runBlocking{ delay(300) } //Put some interval between tests
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
		testingObserver1!!.removeObserver()
		testingObserver2!!.removeObserver()
	}
	
    @Test
    fun testUC1(){
		println("+++++++++ testslotnum ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserverE = Channel<String>()
			val channelForObserverR = Channel<String>()
			testingObserver1!!.addObserver( channelForObserverE,"enter")
			testingObserver2!!.addObserver( channelForObserverR,"receipt")
			
			
			//test if the parkmanagerservice respond with the avaiable parking slot 1 to 6
			for (i in 1..N) {
				val reqin = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
				MsgUtil.sendMsg(reqin, myactor!!)
				result = channelForObserverE.receive()
				println("+++++++++ testslotnum l RESULT=$result")
				assertEquals( result, "enter($i)")
				
				//test if the parkmanagerservice generate the TOKENID: 10, 21, 32, 43, 54, 65
				val reqen = MsgUtil.buildRequest("tester","carenter","carenter($i)","parkmanagerservice")
				MsgUtil.sendMsg(reqen, myactor!!)
				result = channelForObserverR.receive()
				println("+++++++++ testtokenid l RESULT=$result")
				val j = i-1
				assertEquals( result, "receipt($i$j)")
			}
		}	
	}
}