package com.stenleone.stanleysfilm.interfaces.parser

import android.webkit.JavascriptInterface
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class JavaScriptParserVideo(private val callBack: CallBackVideoFromParser) {

    companion object {
        private const val PLAYER_CLASS_NAME = "oframeplayer"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {
        val doc: Document = Jsoup
            .parse(html)

        try {
            callBack.onVideoFind(doc.getElementById(PLAYER_CLASS_NAME).allElements.get(3).allElements.get(1).toString().split("\"").get(1))
        } catch (e: Exception) {
            callBack.onVideoNotFind()
        }

    }
}
