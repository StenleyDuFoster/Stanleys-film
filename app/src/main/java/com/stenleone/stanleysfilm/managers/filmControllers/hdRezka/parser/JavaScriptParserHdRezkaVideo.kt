package com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser

import android.webkit.JavascriptInterface
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class JavaScriptParserHdRezkaVideo(private val callBack: (url: String?) -> Unit) {

    companion object {
        private const val PLAYER_CLASS_NAME = "oframecdnplayer"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {
        val doc: Document = Jsoup
            .parse(html)

        try {
            callBack(doc.getElementById(PLAYER_CLASS_NAME).allElements.get(3).allElements.get(1).toString().split("\"").get(3))
        } catch (e: Exception) {
            callBack(null)
        }

    }
}
