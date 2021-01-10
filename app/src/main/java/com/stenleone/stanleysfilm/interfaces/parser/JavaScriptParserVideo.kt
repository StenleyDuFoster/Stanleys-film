package lampa.test.tmdblib.model.viewmodel.repository.internet.parser

import android.webkit.JavascriptInterface
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserVideo(private val callBack: CallBackVideoFromParser) {

    companion object {
        private const val PLAYER_CLASS_NAME = "oframeplayer"
    }

    @JavascriptInterface
    fun processHTML(html: String?) {

        val doc: Document = Jsoup
            .parse(html)

        val parseArray = doc
            .body()
            .getElementById(PLAYER_CLASS_NAME)
            .getElementsByAttribute("src")
            .toString()
            .split(" \" ")

        if(parseArray.size < 4) {
            val parse = parseArray[0].split("\"")
            callBack.onVideoFind(parse[1])
        }

    }
}
