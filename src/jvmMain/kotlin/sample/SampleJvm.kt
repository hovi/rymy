package sample

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.html.respondHtml
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*
import java.io.File

fun main() {
    parseCss()
    embeddedServer(
        Netty,
        port = 8888,
        watchPaths = listOf("Application"),
        module = Application::module
    ).start(wait = true)
}

val css = """
.k0m0{color:#000000;background-color:#E8E8DD;}
.k0m1{color:#006600;background-color:#E8E8DD;}
.k0m2{color:#660000;background-color:#E8E8DD;}
.k0m3{color:#663300;background-color:#E8E8DD;}
.k0m4{color:#006666;background-color:#E8E8DD;}
.k0m5{color:#666600;background-color:#E8E8DD;}
.k0m6{color:#000066;background-color:#E8E8DD;}
.k0m7{color:#660066;background-color:#E8E8DD;}

.k1m0{color:#000000;background-color:#CCCCFF;}
.k1m1{color:#006600;background-color:#CCCCFF;}
.k1m2{color:#660000;background-color:#CCCCFF;}
.k1m3{color:#663300;background-color:#CCCCFF;}
.k1m4{color:#006666;background-color:#CCCCFF;}
.k1m5{color:#666600;background-color:#CCCCFF;}
.k1m6{color:#000066;background-color:#CCCCFF;}
.k1m7{color:#660066;background-color:#CCCCFF;}

.k2m0{color:#000000;background-color:#BBFFBB;}
.k2m1{color:#006600;background-color:#BBFFBB;}
.k2m2{color:#660000;background-color:#BBFFBB;}
.k2m3{color:#663300;background-color:#BBFFBB;}
.k2m4{color:#006666;background-color:#BBFFBB;}
.k2m5{color:#666600;background-color:#BBFFBB;}
.k2m6{color:#000066;background-color:#BBFFBB;}
.k2m7{color:#660066;background-color:#BBFFBB;}

.k3m0{color:#000000;background-color:#FFBBFF;}
.k3m1{color:#006600;background-color:#FFBBFF;}
.k3m2{color:#660000;background-color:#FFBBFF;}
.k3m3{color:#663300;background-color:#FFBBFF;}
.k3m4{color:#006666;background-color:#FFBBFF;}
.k3m5{color:#666600;background-color:#FFBBFF;}
.k3m6{color:#000066;background-color:#FFBBFF;}
.k3m7{color:#660066;background-color:#FFBBFF;}

.k4m0{color:#000000;background-color:#FFDD88;}
.k4m1{color:#006600;background-color:#FFDD88;}
.k4m2{color:#660000;background-color:#FFDD88;}
.k4m3{color:#663300;background-color:#FFDD88;}
.k4m4{color:#006666;background-color:#FFDD88;}
.k4m5{color:#666600;background-color:#FFDD88;}
.k4m6{color:#000066;background-color:#FFDD88;}
.k4m7{color:#660066;background-color:#FFDD88;}

.k5m0{color:#000000;background-color:#FFAA99;}
.k5m1{color:#006600;background-color:#FFAA99;}
.k5m2{color:#660000;background-color:#FFAA99;}
.k5m3{color:#663300;background-color:#FFAA99;}
.k5m4{color:#006666;background-color:#FFAA99;}
.k5m5{color:#666600;background-color:#FFAA99;}
.k5m6{color:#000066;background-color:#FFAA99;}
.k5m7{color:#660066;background-color:#FFAA99;}

.k6m0{color:#000000;background-color:#BBDDDD;}
.k6m1{color:#006600;background-color:#BBDDDD;}
.k6m2{color:#660000;background-color:#BBDDDD;}
.k6m3{color:#663300;background-color:#BBDDDD;}
.k6m4{color:#006666;background-color:#BBDDDD;}
.k6m5{color:#666600;background-color:#BBDDDD;}
.k6m6{color:#000066;background-color:#BBDDDD;}
.k6m7{color:#660066;background-color:#BBDDDD;}

.k7m0{color:#000000;background-color:#EECC99;}
.k7m1{color:#006600;background-color:#EECC99;}
.k7m2{color:#660000;background-color:#EECC99;}
.k7m3{color:#663300;background-color:#EECC99;}
.k7m4{color:#006666;background-color:#EECC99;}
.k7m5{color:#666600;background-color:#EECC99;}
.k7m6{color:#000066;background-color:#EECC99;}
.k7m7{color:#660066;background-color:#EECC99;}

.k8m0{color:#000000;background-color:#DDDD99;}
.k8m1{color:#006600;background-color:#DDDD99;}
.k8m2{color:#660000;background-color:#DDDD99;}
.k8m3{color:#663300;background-color:#DDDD99;}
.k8m4{color:#006666;background-color:#DDDD99;}
.k8m5{color:#666600;background-color:#DDDD99;}
.k8m6{color:#000066;background-color:#DDDD99;}
.k8m7{color:#660066;background-color:#DDDD99;}

.k9m0{color:#000000;background-color:#FFFF66;}
.k9m1{color:#006600;background-color:#FFFF66;}
.k9m2{color:#660000;background-color:#FFFF66;}
.k9m3{color:#663300;background-color:#FFFF66;}
.k9m4{color:#006666;background-color:#FFFF66;}
.k9m5{color:#666600;background-color:#FFFF66;}
.k9m6{color:#000066;background-color:#FFFF66;}
.k9m7{color:#660066;background-color:#FFFF66;}
    
""".trimIndent()

data class CssRule(val className: String, val color1: String, val color2: String)

fun parseCss() {
    val text = css.split("}").dropLast(1).map {
        "${it.trim()}}"
    }.map {
        CssRule(
            it.substring(1, 5),
            it.substringAfter("{color:").substringBefore(";"),
            it.substringAfter("-color:").substringBefore(";")
        )
    }.map {
        """
            .${it.className} .shoda, .${it.className} .zbytek {
                background-color: ${it.color2}
            }
        """.trimIndent()
    }.joinToString("\n")
    File("./src/jvmMain/resources/style.css").writeText(text)
}

fun Application.module() {
    install(CallLogging)
    install(Routing) {
        get("/test") {
            call.respondHtml {
                head {
                    title("Hello from Ktor!")
                }
                body {
                    +"Hello from Ktor. Check me value"
                    div {
                        id = "js-response"
                        +"Loading..."
                    }
                    +"<div>TEST</div>"
                    script(src = "/rymy.js") {}
                }
            }
        }
        static("/") {
            resource("index.html")
            resource("style.css")
            resource("rymy.js")
        }
    }
}