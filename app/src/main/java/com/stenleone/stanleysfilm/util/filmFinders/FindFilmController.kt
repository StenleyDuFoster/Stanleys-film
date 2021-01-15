package com.stenleone.stanleysfilm.util.filmFinders

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.interfaces.parser.JavaScriptParserPage
import com.stenleone.stanleysfilm.interfaces.parser.JavaScriptParserVideo
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackPageFromParser
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import kotlin.random.Random

class FindFilmController(
    private val activity: Activity,
    private val webView: WebView,
    private val titleMovie: String,
    private val dateMovie: String,
    private val loadVideoCallBack: CallBackVideoFromParser
) : CallBackPageFromParser {

    companion object {
        const val TAG = "FindFilmController"
        const val FAILED_FIND = -1
        private const val FILM_IX_BASE_URL = "https://filmix.co/"
        private const val FILM_IX_SEARCH = "${FILM_IX_BASE_URL}search/"
        private const val USER_AGENT_WEB_VIEW = "Chrome/41.0.2228.0 Safari/537.36"
        private const val MAX_FOUND_WITH_DATE_TRY = 3
        private const val MAX_TRY = 6
    }

    val progress = MutableLiveData<Int>()
    val status = MutableLiveData<String>()

    private var countFoundTry = 1

    init {
        findPageUrlWithVideoView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun findPageUrlWithVideoView() {

        progress.postValue(0)
        status.postValue("ищем страницу \"$titleMovie\"")

        activity.runOnUiThread {
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserPage(this), "PAGE")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {

                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            {
                                webView.loadUrl("javascript:window.PAGE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")

                                status.postValue("парсинг страницы")
                                val oldProgress = progress.value ?: 0
                                val newProgress = 30 + Random.nextInt(-2, 20)

                                if (newProgress > oldProgress) {
                                    progress.postValue(newProgress)
                                }
                            }, 1000 // Sometimes the webView does not have time to load, this waiting fixes problem
                        )
                }
            }
            webView.settings.userAgentString = USER_AGENT_WEB_VIEW

            val dataValue: String = if (countFoundTry < MAX_FOUND_WITH_DATE_TRY) {
                "-${dateMovie.substring(0, 4)}"
            } else {
                ""
            }

            Log.v(TAG, "$FILM_IX_SEARCH${titleMovie.replace(" ", "+")}$dataValue")
            webView.loadUrl("$FILM_IX_SEARCH${titleMovie.replace(" ", "+")}$dataValue")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun findVideoUrlWithVideoView(pageUrl: String) {
        activity.runOnUiThread {

            status.postValue("поиск видео")
            val oldProgress = progress.value ?: 0
            val newProgress = 80 + Random.nextInt(-2, 10)

            if (newProgress > oldProgress) {
                progress.postValue(newProgress)
            }

            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserVideo(loadVideoCallBack), "VIDEO")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    webView.loadUrl("javascript:window.VIDEO.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                }
            }
            webView.settings.userAgentString = USER_AGENT_WEB_VIEW

            webView.loadUrl(pageUrl)
        }
    }

    override fun onPageFind(link: String) {
        Log.v(TAG, link)
        findVideoUrlWithVideoView(link)
    }

    override fun onPageNotFound() {

        activity.runOnUiThread {
            countFoundTry++

            if (countFoundTry < MAX_TRY) {
                findPageUrlWithVideoView()
                val oldProgress = progress.value ?: 0
                val newProgress = 50 + Random.nextInt(1, 20)

                if (newProgress > oldProgress) {
                    progress.postValue(newProgress)
                }
                status.postValue("перезагрузка страницы")
            } else {
                progress.postValue(FAILED_FIND)
                status.postValue("не удалось найти страницу")
            }
        }
    }
}