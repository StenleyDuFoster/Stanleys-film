package com.stenleone.stanleysfilm.managers.filmControllers.filmix

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.FAILED_FIND
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.MAX_FOUND_WITH_DATE_TRY
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.MAX_TRY
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.USER_AGENT_WEB_VIEW
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser.JavaScriptParserFilmixPage
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser.JavaScriptParserFilmixVideo
import com.stenleone.stanleysfilm.managers.firebase.FirebaseRemoteConfigManager
import com.stenleone.stanleysfilm.model.entity.FirebaseConfigsEnum
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class FindFilmFilmixController @Inject constructor(
    @ApplicationContext val context: Context,
    firebaseRemoteConfigManager: FirebaseRemoteConfigManager
) : FindFilmController {

    private val controllerJob = Job()
    private val controllerScope = CoroutineScope(Main + controllerJob)

    companion object {
        const val TAG = "FindFilmFilmix"
    }

    override val progress = MutableLiveData<Int>()
    override val status = MutableLiveData<String>()

    private lateinit var webView: WebView
    private var countFoundTry = 1
    private lateinit var titleMovie: String
    private lateinit var dateMovie: String
    private lateinit var loadVideoCallBack: (String?) -> Unit
    private var webViewInProgress = false
    private val filmixBaseUrl: String = firebaseRemoteConfigManager.getString(FirebaseConfigsEnum.FILM_IX_BASE_URL)
    private val filmixSearchUrl: String = "${filmixBaseUrl}search/"

    override fun start(
        titleMovie: String,
        dateMovie: String,
        loadVideoCallBack: (String?) -> Unit
    ) {
        webView = WebView(context)
        this.titleMovie = titleMovie
        this.dateMovie = dateMovie
        this.loadVideoCallBack = loadVideoCallBack
        findPageUrlWithVideoView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun findPageUrlWithVideoView() {

        progress.postValue(0)
        status.postValue("ищем страницу \"$titleMovie\"")

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JavaScriptParserFilmixPage { if (it != null) onPageFind(it) else onPageNotFound() }, "PAGE")
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

        Log.v(TAG, "$filmixSearchUrl${titleMovie.replace(" ", "+")}$dataValue")
        webView.loadUrl("$filmixSearchUrl${titleMovie.replace(" ", "+")}$dataValue")

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun findVideoUrlWithVideoView(pageUrl: String) {
        webViewInProgress = true
        controllerScope.launch {
            status.postValue("поиск видео")
            val oldProgress = progress.value ?: 0
            val newProgress = 80 + Random.nextInt(-2, 10)

            if (newProgress > oldProgress) {
                progress.postValue(newProgress)
            }

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = false
            webView.addJavascriptInterface(JavaScriptParserFilmixVideo(loadVideoCallBack), "VIDEO")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            {
                                webView.loadUrl("javascript:window.VIDEO.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                                webViewInProgress = false

                            }, 5000 // Sometimes the webView does not have time to load, this waiting fixes problem
                        )

                }
            }
            webView.settings.userAgentString = USER_AGENT_WEB_VIEW

            webView.loadUrl(pageUrl)
        }
    }

    private fun onPageFind(link: String) {
        Log.v(TAG, link)
        if (!webViewInProgress) {
            controllerScope.launch {
                findVideoUrlWithVideoView(link)
            }
        }
    }

    private fun onPageNotFound() {

        countFoundTry++
        controllerScope.launch {
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

    override fun onDestroy() {
        controllerJob.cancel()
        webView.destroy()
    }
}