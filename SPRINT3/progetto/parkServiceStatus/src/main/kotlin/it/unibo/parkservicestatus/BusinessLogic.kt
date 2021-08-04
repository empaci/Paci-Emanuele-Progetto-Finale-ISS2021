package it.unibo.parkservicestatus

import com.andreapivetta.kolor.Color
import connSupport.ConnectionType
import connSupport.connQakBase
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import it.unibo.actor0.sysUtil
import org.springframework.web.util.HtmlUtils


object BusinessLogic {

    var conn : connQakBase = connQakBase.create(ConnectionType.TCP)

    init {
        conn.createConnection()
    }

    fun requestIn() : String {
        val reqen = MsgUtil.buildRequest("webgui","clientRequest","clientRequest(in)","parkmanagerservice")
        val response = conn.request(reqen)

        val answ = response.split("(",")").elementAt(2)

        return answ
    }

    fun requestOut(tokenid : String?) {

        val reqen = MsgUtil.buildDispatch("webgui","outTokenid","outTokenid($tokenid)","parkmanagerservice")
        val response = conn.forward(reqen)
    }

    fun carenter(slotnum : String?) : String {
        val reqen = MsgUtil.buildRequest("webgui","carenter","carenter($slotnum)","parkmanagerservice")
        val response = conn.request(reqen)

        val answ = response.split("(",")").elementAt(2)

        return answ
    }

}