package com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser

import android.webkit.JavascriptInterface
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import com.stenleone.stanleysfilm.model.entity.FilmUrlData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class JavaScriptParserFilmixVideo(private val callBack: (url: FilmUrlData?) -> Unit) {

    companion object {
        private const val PLAYER_CLASS_NAME = "oframeplayer"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {
        val doc: Document = Jsoup
            .parse(html)

        try {

            val filmUrlData = FilmUrlData(
                provider = FilmControllerEnum.FILMIX
            )

            filmUrlData.url_360 = doc.getElementById(PLAYER_CLASS_NAME).allElements.get(3).allElements.get(1).toString().split("\"").get(1)

            callBack(filmUrlData)
        } catch (e: Exception) {
            callBack(null)
        }

    }
}
