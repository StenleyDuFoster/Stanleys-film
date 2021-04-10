package com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser

import android.webkit.JavascriptInterface
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class JavaScriptParserHdRezkaPage(private val callBack: (url: String?) -> Unit) {

    companion object {
        private const val BUTTON_WATCH_CLASS_NAME = "b-content__inline_item-cover"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {

        val doc: Document = Jsoup
            .parse(html)

        val parsePage = doc
            .body()
            .getElementsByClass(BUTTON_WATCH_CLASS_NAME)

        // I don’t remember at all what kind of crap it is and how it works, I wrote it under something

        if (parsePage.size > 0) {

            try {
                val parse = parsePage.toString().split("<").get(2)

                callBack(parse.substring(8, parse.length - 3))
            } catch (e: Exception) {
                callBack(null)
            }
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
