package it.unibo.parkservicestatus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableWebMvc		//DO NOT USE: disbale satic
//@EnableWebMvc will disable org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
class ParkservicestatusApplication

fun main(args: Array<String>) {
    runApplication<ParkservicestatusApplication>(*args)
}