package sample

import com.github.hovi.kotlintools.dom.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.khronos.webgl.DataView
import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window


fun main() {
    document.getHtmlElementById<HTMLInputElement>("search").addClickListener {
        search()
    }

    document.getHtmlElementById<HTMLInputElement>("settings").addClickListener {
        settingsWrap.classList.toggle("show")
    }

    searchInput.addEventListener("keydown", {
        val keyEvent = it as KeyboardEvent
        if (keyEvent.keyCode == 13) {
            search()
        }
    })

    searchInput.addKeyboardListener("keydown") {
        if (it.keyCode == 13) {
            search()
        }
    }


}

val settingsWrap = document.getHtmlElementById<HTMLElement>("settings-wrap")
val resultsWrap = document.getHtmlElementById<HTMLElement>("results")
val searchInput = document.getHtmlElementById<HTMLInputElement>("wdUp")
const val rymycz = "http://rymy.cz"
const val corsAnywhereHeroku = "https://cors-anywhere.herokuapp.com/"

fun requestAjax(
    params: String,
    proxy: String = corsAnywhereHeroku,
    method: String = "POST",
    charset: String = "windows-1250",
    callback: (String) -> Unit
) {
    val xhr = XMLHttpRequest()
    xhr.open(method, "${proxy}${rymycz}", true)
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=${charset}")

    xhr.onreadystatechange = {
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200.toShort()) {
            callback(xhr.responseText)
        }
    }
    xhr.onerror = {

    }
    xhr.send(params)
}

val radioNames = arrayOf(
    "pfPG",
    "pfPP",
    "pfPC",
    "pfPV",
    "pfPK",
    "pfPH",
    "pfPE",
    "pfPA",
    "pfPZ"
)

fun params(): String {
    val values = radioNames.joinToString("&") {
        val value = (document.querySelector("input[name=${it}]:checked") as HTMLInputElement).value
        "${it}=${value}"
    }
    return "${values}&${encodeValue("wdUp")}&${encodeValue("lnPS")}&btFindUp=Vyhledat&pgNext=2&wdLo="
}

suspend fun requestFetch(callback: (String) -> Unit) {
    val myHeaders = Headers()
    myHeaders.append(
        "content-Type", "application/x-www-form-urlencoded;charset=windows-1250"
    )
    val params = "pfPG=2&pfPP=2&pfPC=2&pfPV=2&pfPK=2&pfPH=2&pfPE=2&pfPA=2&pfPZ=2&lnPS=128&${encodeValue(
        "wdUp"
    )}&btFindUp=Vyhledat&pgNext=2&wdLo="
    val result = window.fetch(
        "${corsAnywhereHeroku}${rymycz}", RequestInit(
            method = "POST",
            headers = myHeaders,
            body = URLSearchParams(
                params
            )
        )
    ).await()
    val bytes = result.arrayBuffer().await()
    val dataView = DataView(bytes)
    val decoder = js("""new TextDecoder("windows-1250");""")
    val text = decoder.decode(dataView) as String
    callback(text)
}

fun search() {
    resultsWrap.innerHTML = ""
    settingsWrap.classList.remove("show")
    if (searchInput.value.isEmpty()) {
        return
    }
    GlobalScope.launch {
        requestAjax(params = params(), callback = ::finishText)
    }
}

fun finishText(t: String) {
    var text = t
    val div = document.createHtmlElement<HTMLDivElement>()
    text = text.substringAfter("""<div class='rymdiv'>""").substringAfter("<br />")
        .substringAfter("""<a href="./forum/">Fórum</a>""")
    text = text.substringBefore("</div></form>")
    div.innerHTML = text
    div.querySelectorAll("div").asElementList<HTMLDivElement>().filter {
        it.className.matches("k[0-9]m[0-9]".toRegex())
    }.forEach {
        val row: HTMLTableRowElement = document.createHtmlElement()
        row.className = it.className
        row.innerHTML = it.toNewHtml()
        resultsWrap.appendChild(row)
    }
}

fun encodeValue(id: String): String {
    var value = document.getHtmlElementById<HTMLElement>(id).formValue
    charReplacementsWindows1250.forEach {
        value = value.replace(it.key, it.value)
    }
    return "${id}=${value}"
}


fun HTMLElement.toNewHtml(): String {
    return Result.parse(this.innerHTML, this.className).toTDHtml()
}