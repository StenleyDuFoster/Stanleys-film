package com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser

import android.webkit.JavascriptInterface
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import com.stenleone.stanleysfilm.model.entity.FilmUrlData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserHdRezkaBlopVideo(private val callBack: (url: FilmUrlData?) -> Unit) {

    companion object {
        private const val SCRIPT_UTL_INDEX = 7

        private const val Q_360 = "[360p]"
        private const val Q_480 = "[480p]"
        private const val Q_720 = "[720p]"
        private const val Q_1080 = "[1080p]"
        private const val Q_1080_ULTRA = "[1080p Ultra]"
    }

    private var findElement = false

    @JavascriptInterface
    fun processHTML(html: String?) {
        val doc: Document = Jsoup
            .parse(html)

        try {

            val docSize = doc.body().allElements.size
            val scriptElements = doc.body().allElements.get(docSize - SCRIPT_UTL_INDEX).allElements.toString().split("\"streams\":").get(1).split(",")

            val filmUrlData = FilmUrlData(
                provider = FilmControllerEnum.HD_REZKA
            )

            scriptElements.forEach {
                if (it.contains(Q_1080_ULTRA)) {
                    findElement = true
                    var url = it.replace("\\", "").replace(Q_1080_ULTRA, "")

                    if (url.contains("or")) {
                        url = url.split("or").get(1)
                    }

                    filmUrlData.url_2060 = url.replace("\"", "")
                } else if (it.contains(Q_1080)) {
                    findElement = true
                    var url = it.replace("\\", "").replace(Q_1080, "")

                    if (url.contains("or")) {
                        url = url.split("or").get(1)
                    }

                    filmUrlData.url_1080 = url.replace("\"", "")
                } else if (it.contains(Q_720)) {
                    findElement = true
                    var url = it.replace("\\", "").replace(Q_720, "")

                    if (url.contains("or")) {
                        url = url.split("or").get(1)
                    }

                    filmUrlData.url_720 = url.replace("\"", "")
                } else if (it.contains(Q_480)) {
                    findElement = true
                    var url = it.replace("\\", "").replace(Q_480, "")

                    if (url.contains("or")) {
                        url = url.split("or").get(1)
                    }

                    filmUrlData.url_480 = url.replace("\"", "")
                } else if (it.contains(Q_360)) {
                    findElement = true
                    var url = it.replace("\\", "").replace(Q_360, "")

                    if (url.contains("or")) {
                        url = url.split("or").get(1)
                    }

                    filmUrlData.url_360 = url.replace("\"", "")
                }
            }

            if (!findElement) {
                callBack(null)
            } else {
                callBack(filmUrlData)
            }

        } catch (e: Exception) {
            callBack(null)
        }

    }
}
