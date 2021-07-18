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
		val channelSyncStart      = Channel<String>()
		var testingObserver       : CoapObserverForTesting? = null
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
				testingObserver = CoapObserverForTesting()
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
		testingObserver!!.removeObserver()
	}
	
    @Test
    fun testUC1(){
		println("+++++++++ testslotnum ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserverL = Channel<String>()
			
			//test if the parkmanagerservice respond with the avaiable parking slot 1
			testingObserver!!.addObserver( channelForObserverL,"enter")
			val reqin = MsgUtil.buildRequest("tester","clientRequest","clientRequest(in)","parkmanagerservice")
			MsgUtil.sendMsg(reqin, myactor!!)
			result = channelForObserverL.receive()
			println("+++++++++ testslotnum l RESULT=$result")
			assertEquals( result, "enter(1)")
			testingObserver!!.removeObserver()
			
			//test if the parkmanagerservice generate the TOKENID 10
			testingObserver!!.addObserver( channelForObserverL,"receipt")
			val reqen = MsgUtil.buildRequest("tester","carenter","carenter(1)","parkmanagerservice")
			MsgUtil.sendMsg(reqen, myactor!!)
			//MsgUtil.sendMsg("carenter","carenter(1)",myactor!!)
			result = channelForObserverL.receive()
			println("+++++++++ testtokenid l RESULT=$result")
			assertEquals( result, "receipt(10)")
			testingObserver!!.removeObserver()
		}	
	}
}