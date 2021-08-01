package it.unibo.parkservicestatus

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class WebPageCoapHandler(val controller: HIController) : CoapHandler {
    var counter = 0
    override fun onLoad(response: CoapResponse) {
        val content: String = StateAdapter.convertToJSON(response.getResponseText())
        sysUtil.colorPrint("WebPageCoapHandler | response content=$content}", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("thermometer")){
                if (jsonContent.getString("thermometer").contains("warning")){ //add below to clear the area
                    val critRep = ResourceRep(" Warnining " + HtmlUtils.htmlEscape( jsonContent.getString("thermometer").removePrefix("warning"))  )
                    sysUtil.colorPrint("WebPageCoapHandler | critic temp value=${critRep.content}", Color.BLUE)
                    controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, critRep)
                } else {
                    val tempRep = ResourceRep(" temperature " + HtmlUtils.htmlEscape(jsonContent.getString("thermometer")))
                    sysUtil.colorPrint("WebPageCoapHandler | temperature value=${tempRep.content}", Color.BLUE)
                    controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, tempRep)
                }
            }
            if (jsonContent.has("fan")){
                val fanRep = ResourceRep(" fan " + HtmlUtils.htmlEscape( jsonContent.getString("fan"))  )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${fanRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fanRep)
            }
            if (jsonContent.has("trolley")) {
                val trolleyRep = ResourceRep(" trolley " + HtmlUtils.htmlEscape(jsonContent.getString("trolley")))
                sysUtil.colorPrint("WebPageCoapHandler | trolley value=${trolleyRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, trolleyRep)
            }
            if (jsonContent.has("sonar")) {
                val sonarRep = ResourceRep(" sonar " + HtmlUtils.htmlEscape(jsonContent.getString("sonar")) )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${sonarRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, sonarRep)
            }
            if (jsonContent.has("weight")) {
                val weightRep = ResourceRep(" weight " + HtmlUtils.htmlEscape(jsonContent.getString("weight")) )
                sysUtil.colorPrint("WebPageCoapHandler | weight value=${weightRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, weightRep)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("WebPageCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}