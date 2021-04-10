package com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser

import android.webkit.JavascriptInterface
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserFilmixPage(private val callBack: (url: String?) -> Unit) {

    companion object {
        private const val BUTTON_WATCH_CLASS_NAME = "watch icon-play"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {

        val doc: Document = Jsoup
            .parse(html)

        val parsePage = doc
            .body()
            .getElementsByClass(BUTTON_WATCH_CLASS_NAME)

        // I don’t remember at all what kind of crap it is and how it works, I wrote it under something

        if (parsePage.size > 4) {
            val parse = parsePage[0].toString().split("\"")
            callBack(parse[5])
        } else {
            if(!parsePage.isNullOrEmpty()) {
                val parse = parsePage[0].toString().split("\"")
                callBack(parse[5])
            } else {
                callBack(null)
            }
        }
    }
}
